package com.simats.formsahayak.ui.viewmodel

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.simats.formsahayak.logic.*
import com.simats.formsahayak.ui.screens.Language
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream

data class RecentScan(
    val name: String,
    val bitmap: Bitmap,
    val formType: String,
    val fields: List<DetectedField>
)

data class UserData(
    val fullName: String,
    val emailOrPhone: String,
    val password: String
)

class FormViewModel : ViewModel() {
    var loggedInUser by mutableStateOf<UserData?>(null)
        private set

    var profilePicture by mutableStateOf<Bitmap?>(null)
        private set

    var capturedBitmap by mutableStateOf<Bitmap?>(null)
        private set

    val scannedPages = mutableStateListOf<Bitmap>()
    var currentGuidingPageIndex by mutableIntStateOf(0)
    
    var detectedFormType by mutableStateOf("")
        private set

    var detectedAccountType by mutableStateOf("General")
        private set
        
    var detectedConfidence by mutableIntStateOf(85)
        private set
        
    var detectedFields by mutableStateOf<List<DetectedField>>(emptyList())
        private set
        
    var isLoading by mutableStateOf(false)
        private set
        
    var errorMessage by mutableStateOf<String?>(null)
        private set

    var voiceSpeed by mutableStateOf("Normal")
        private set

    var backendGuidance by mutableStateOf("")
        private set

    val recentScans = mutableStateListOf<RecentScan>()

    private var ttsHelper: TtsHelper? = null

    fun initTts(context: Context) {
        if (ttsHelper == null) {
            ttsHelper = TtsHelper(context)
            updateVoiceSpeed(voiceSpeed)
        }
    }

    fun addScannedPage(bitmap: Bitmap) {
        scannedPages.add(bitmap)
    }

    fun removeScannedPage(index: Int) {
        if (index in scannedPages.indices) {
            scannedPages.removeAt(index)
        }
    }

    // --- API Integration ---
    fun signup(name: String, email: String, password: String, confirmPassword: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val request = SignupRequest(name, email, password, confirmPassword)
                val response = RetrofitClient.apiService.signup(request)
                if (response.isSuccessful) {
                    onResult(true, response.body()?.message ?: "Signup successful")
                } else {
                    val message = parseError(response.errorBody()?.string())
                    errorMessage = message
                    onResult(false, message)
                }
            } catch (e: Exception) {
                errorMessage = "Connection error: ${e.message}"
                onResult(false, errorMessage!!)
            } finally {
                isLoading = false
            }
        }
    }

    fun login(email: String, password: String, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val request = LoginRequest(email, password)
                val response = RetrofitClient.apiService.login(request)
                if (response.isSuccessful) {
                    val loginResponse = response.body()
                    loggedInUser = UserData(loginResponse?.user?.name ?: "", loginResponse?.user?.email ?: "", "")
                    onResult(true, loginResponse?.message ?: "Login successful")
                } else {
                    val message = parseError(response.errorBody()?.string())
                    errorMessage = message
                    onResult(false, message)
                }
            } catch (e: Exception) {
                errorMessage = "Connection error: ${e.message}"
                onResult(false, errorMessage!!)
            } finally {
                isLoading = false
            }
        }
    }

    fun uploadDocument(context: Context, bitmap: Bitmap, email: String, onComplete: () -> Unit = {}) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            backendGuidance = ""
            try {
                // Convert bitmap to file
                val file = File(context.cacheDir, "upload_image.jpg")
                val bos = ByteArrayOutputStream()
                bitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos)
                val bitmapData = bos.toByteArray()
                val fos = FileOutputStream(file)
                fos.write(bitmapData)
                fos.flush()
                fos.close()

                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                val body = MultipartBody.Part.createFormData("file", file.name, requestFile)
                val userEmail = email.toRequestBody("text/plain".toMediaTypeOrNull())

                val response = RetrofitClient.apiService.uploadDocument(userEmail, body)
                if (response.isSuccessful) {
                    val uploadResponse = response.body()
                    backendGuidance = uploadResponse?.guidance ?: "No guidance provided"
                    // Optionally speak the guidance
                    speak(backendGuidance, "en")
                } else {
                    errorMessage = parseError(response.errorBody()?.string())
                }
            } catch (e: Exception) {
                errorMessage = "Upload failed: ${e.message}"
            } finally {
                isLoading = false
                onComplete()
            }
        }
    }

    private fun parseError(errorBody: String?): String {
        if (errorBody.isNullOrEmpty()) return "Unknown error"
        return try {
            val json = JSONObject(errorBody)
            val detail = json.opt("detail")
            when (detail) {
                is JSONArray -> detail.getJSONObject(0).optString("msg", "Validation error")
                is String -> detail
                else -> "An error occurred"
            }
        } catch (e: Exception) {
            "An error occurred"
        }
    }

    fun logout() {
        loggedInUser = null
        profilePicture = null
        reset()
    }

    fun updateProfilePicture(bitmap: Bitmap?) {
        profilePicture = bitmap
    }

    fun getBitmapFromUri(context: Context, uri: Uri): Bitmap? {
        return try {
            if (Build.VERSION.SDK_INT < 28) {
                @Suppress("DEPRECATION")
                MediaStore.Images.Media.getBitmap(context.contentResolver, uri)
            } else {
                val source = ImageDecoder.createSource(context.contentResolver, uri)
                ImageDecoder.decodeBitmap(source) { decoder, _, _ ->
                    decoder.isMutableRequired = true
                }
            }
        } catch (e: Exception) {
            null
        }
    }

    // --- Image Manipulation ---
    fun rotateBitmap(bitmap: Bitmap): Bitmap {
        val matrix = Matrix()
        matrix.postRotate(90f)
        return Bitmap.createBitmap(bitmap, 0, 0, bitmap.width, bitmap.height, matrix, true)
    }

    fun cropBitmap(bitmap: Bitmap): Bitmap {
        val width = bitmap.width
        val height = bitmap.height
        val left = (width * 0.05).toInt()
        val top = (height * 0.05).toInt()
        val right = (width * 0.95).toInt()
        val bottom = (height * 0.95).toInt()
        return Bitmap.createBitmap(bitmap, left, top, right - left, bottom - top)
    }

    fun applyEnhancements(bitmap: Bitmap, brightness: Float, contrast: Float): Bitmap {
        val config = bitmap.config ?: Bitmap.Config.ARGB_8888
        val output = Bitmap.createBitmap(bitmap.width, bitmap.height, config)
        val canvas = Canvas(output)
        val paint = Paint()
        
        val matrix = ColorMatrix()
        val scale = contrast
        val translate = (brightness - 1.0f) * 255f
        
        matrix.set(floatArrayOf(
            scale, 0f, 0f, 0f, translate,
            0f, scale, 0f, 0f, translate,
            0f, 0f, scale, 0f, translate,
            0f, 0f, 0f, 1f, 0f
        ))
        
        paint.colorFilter = ColorMatrixColorFilter(matrix)
        canvas.drawBitmap(bitmap, 0f, 0f, paint)
        return output
    }

    fun updateVoiceSpeed(speed: String) {
        voiceSpeed = speed
        val rate = when (speed) {
            "Slow" -> 0.75f
            "Normal" -> 1.0f
            "Fast" -> 1.25f
            else -> 1.0f
        }
        ttsHelper?.setSpeechRate(rate)
    }

    fun analyzeImage(context: Context, bitmap: Bitmap, language: Language?, onResult: () -> Unit) {
        val analyzer = FormAnalyzer(context)
        isLoading = true
        errorMessage = null

        analyzer.analyzeForm(bitmap, language?.code ?: "en") { result ->
            isLoading = false
            result.onSuccess { analysis ->
                capturedBitmap = bitmap
                detectedFormType = analysis.formType
                detectedAccountType = analysis.bankType
                detectedConfidence = analysis.confidence
                detectedFields = analysis.fields

                if (recentScans.none { it.bitmap == bitmap }) {
                    recentScans.add(0, RecentScan(analysis.formType, bitmap, analysis.formType, analysis.fields))
                }

                // Also trigger backend upload for AI guidance
                uploadDocument(context, bitmap, loggedInUser?.emailOrPhone ?: "guest@example.com")
            }.onFailure { e ->
                errorMessage = e.message
            }
            onResult()
        }
    }

    fun selectScan(scan: RecentScan) {
        capturedBitmap = scan.bitmap
        detectedFormType = scan.formType
        detectedFields = scan.fields
    }

    fun speak(text: String, languageCode: String) {
        ttsHelper?.speak(text, languageCode)
    }

    fun speakFieldInstruction(field: DetectedField, languageCode: String) {
        ttsHelper?.speak(field.instruction, languageCode)
    }

    fun clearError() {
        errorMessage = null
    }

    fun reset() {
        capturedBitmap = null
        scannedPages.clear()
        currentGuidingPageIndex = 0
        detectedFormType = ""
        detectedAccountType = "General"
        detectedConfidence = 85
        detectedFields = emptyList()
        backendGuidance = ""
        errorMessage = null
    }

    override fun onCleared() {
        super.onCleared()
        ttsHelper?.shutdown()
    }
}
