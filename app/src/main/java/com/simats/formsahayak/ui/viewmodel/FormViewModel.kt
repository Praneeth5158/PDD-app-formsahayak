package com.simats.formsahayak.ui.viewmodel

import android.content.Context
import android.graphics.*
import android.net.Uri
import android.os.Build
import android.provider.MediaStore
import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.exoplayer.ExoPlayer
import com.simats.formsahayak.logic.*
import com.simats.formsahayak.ui.screens.Language
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import org.json.JSONArray
import org.json.JSONObject
import java.io.ByteArrayOutputStream
import java.io.File
import java.io.FileOutputStream
import java.net.URL

data class RecentScan(
    val name: String,
    val bitmap: Bitmap,
    val formType: String,
    val fields: List<DetectedField>
)

data class UserData(
    val fullName: String,
    val emailOrPhone: String,
    val phone: String = "",
    val language: String = "en",
    val profileImageUrl: String? = null
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

    var backendAudioUrl by mutableStateOf<String?>(null)
        private set

    var backendPdfUrl by mutableStateOf<String?>(null)
        private set

    var isAudioPlaying by mutableStateOf(false)
        private set

    var currentlyPlayingUrl by mutableStateOf<String?>(null)
        private set

    var isBackendProcessing by mutableStateOf(false)
        private set

    val recentScans = mutableStateListOf<RecentScan>()
    
    var formsHistory = mutableStateListOf<HistoryItem>()
        private set

    var selectedFormDetails by mutableStateOf<FormDetailsResponse?>(null)
        private set
        
    var selectedFormBitmap by mutableStateOf<Bitmap?>(null)
        private set

    private var ttsHelper: TtsHelper? = null
    private var exoPlayer: ExoPlayer? = null

    fun initTts(context: Context) {
        if (ttsHelper == null) {
            ttsHelper = TtsHelper(context)
            updateVoiceSpeed(voiceSpeed)
        }
        if (exoPlayer == null) {
            exoPlayer = ExoPlayer.Builder(context.applicationContext).build().apply {
                addListener(object : Player.Listener {
                    override fun onIsPlayingChanged(isPlaying: Boolean) {
                        isAudioPlaying = isPlaying
                        if (!isPlaying) {
                            currentlyPlayingUrl = null
                        }
                    }
                    override fun onPlaybackStateChanged(state: Int) {
                        if (state == Player.STATE_ENDED) {
                            isAudioPlaying = false
                            currentlyPlayingUrl = null
                            Log.d("AUDIO_DEBUG", "audio playback completed")
                        }
                    }
                    override fun onPlayerError(error: androidx.media3.common.PlaybackException) {
                        Log.e("AUDIO_DEBUG", "audio playback failed: ${error.message}")
                        isAudioPlaying = false
                        currentlyPlayingUrl = null
                    }
                })
            }
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
                    loggedInUser = UserData(loginResponse?.user?.name ?: "", loginResponse?.user?.email ?: "")
                    fetchProfile(loginResponse?.user?.email ?: "")
                    fetchHistory(loginResponse?.user?.email ?: "")
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

    fun changePassword(email: String, newPass: String, confirmPass: String, onResult: (Boolean, String) -> Unit) {
        if (newPass.isBlank() || confirmPass.isBlank()) {
            onResult(false, "Passwords cannot be empty")
            return
        }
        if (newPass != confirmPass) {
            onResult(false, "Passwords do not match")
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val request = ChangePasswordRequest(email, newPass, confirmPass)
                val response = RetrofitClient.apiService.changePassword(request)
                if (response.isSuccessful) {
                    onResult(true, response.body()?.message ?: "Password updated successfully")
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

    fun sendOtp(email: String, onResult: (Boolean, String) -> Unit) {
        val cleanEmail = email.trim()
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            Log.d("FormViewModel", "sendOtp Request: email=$cleanEmail")
            Log.d("FORGOT_PASSWORD_FLOW", "SEND_OTP_API_START")
            try {
                val request = SendOtpRequest(cleanEmail)
                val response = RetrofitClient.apiService.sendOtp(request)
                Log.d("FormViewModel", "sendOtp Response Code: ${response.code()}")
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("FORGOT_PASSWORD_FLOW", "SEND_OTP_SUCCESS")
                    Log.d("FormViewModel", "sendOtp Success Body: $body")
                    onResult(true, body?.message ?: "OTP sent")
                } else {
                    val errorString = response.errorBody()?.string()
                    Log.d("FORGOT_PASSWORD_FLOW", "SEND_OTP_ERROR")
                    Log.e("FormViewModel", "sendOtp Error Body: $errorString")
                    val message = parseError(errorString)
                    errorMessage = message
                    onResult(false, message)
                }
            } catch (e: Exception) {
                Log.d("FORGOT_PASSWORD_FLOW", "SEND_OTP_EXCEPTION")
                Log.e("FormViewModel", "sendOtp Exception: ${e.message}", e)
                errorMessage = "Connection error: ${e.message}"
                onResult(false, errorMessage!!)
            } finally {
                isLoading = false
            }
        }
    }

    fun verifyOtp(email: String, otp: String, onResult: (Boolean, String) -> Unit) {
        val cleanEmail = email.trim()
        val cleanOtp = otp.trim()
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            Log.d("FormViewModel", "verifyOtp Request: email=$cleanEmail, otp=$cleanOtp")
            try {
                val request = VerifyOtpRequest(cleanEmail, cleanOtp)
                val response = RetrofitClient.apiService.verifyOtp(request)
                Log.d("FormViewModel", "verifyOtp Response Code: ${response.code()}")
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("FormViewModel", "verifyOtp Success Body: $body")
                    onResult(true, body?.message ?: "OTP verified")
                } else {
                    val errorString = response.errorBody()?.string()
                    Log.e("FormViewModel", "verifyOtp Error Body: $errorString")
                    val message = parseError(errorString)
                    errorMessage = message
                    onResult(false, message)
                }
            } catch (e: Exception) {
                Log.e("FormViewModel", "verifyOtp Exception: ${e.message}", e)
                errorMessage = "Connection error: ${e.message}"
                onResult(false, errorMessage!!)
            } finally {
                isLoading = false
            }
        }
    }

    fun resetPassword(email: String, newPass: String, confirmPass: String, onResult: (Boolean, String) -> Unit) {
        val cleanEmail = email.trim()
        val cleanNewPass = newPass.trim()
        val cleanConfirmPass = confirmPass.trim()
        
        if (cleanNewPass.isBlank() || cleanConfirmPass.isBlank()) {
            onResult(false, "Passwords cannot be empty")
            return
        }
        if (cleanNewPass != cleanConfirmPass) {
            onResult(false, "Passwords do not match")
            return
        }
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            Log.d("FormViewModel", "resetPassword Request: email=$cleanEmail")
            try {
                val request = ResetPasswordRequest(cleanEmail, cleanNewPass, cleanConfirmPass)
                val response = RetrofitClient.apiService.resetPassword(request)
                Log.d("FormViewModel", "resetPassword Response Code: ${response.code()}")
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("FormViewModel", "resetPassword Success Body: $body")
                    onResult(true, body?.message ?: "Password reset successfully")
                } else {
                    val errorString = response.errorBody()?.string()
                    Log.e("FormViewModel", "resetPassword Error Body: $errorString")
                    val message = parseError(errorString)
                    errorMessage = message
                    onResult(false, message)
                }
            } catch (e: Exception) {
                Log.e("FormViewModel", "resetPassword Exception: ${e.message}", e)
                errorMessage = "Connection error: ${e.message}"
                onResult(false, errorMessage!!)
            } finally {
                isLoading = false
            }
        }
    }

    fun fetchProfile(email: String) {
        viewModelScope.launch {
            try {
                val response = RetrofitClient.apiService.getProfile(email)
                if (response.isSuccessful) {
                    val profile = response.body()
                    if (profile != null) {
                        loggedInUser = UserData(
                            fullName = profile.name,
                            emailOrPhone = profile.email,
                            phone = profile.phone ?: "",
                            language = profile.language ?: "en",
                            profileImageUrl = profile.profileImage
                        )
                        // Load profile image from URL if available
                        profile.profileImage?.let { imageUrl ->
                            if (imageUrl.isNotEmpty()) {
                                loadProfileImage(imageUrl)
                            }
                        }
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    private fun loadProfileImage(imageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fullUrl = UrlHelper.cleanUrl(imageUrl)
                Log.d("PROFILE_DEBUG", "profile image URL received: $fullUrl")
                val url = URL(fullUrl)
                val connection = url.openConnection()
                connection.doInput = true
                connection.connect()
                val input = connection.getInputStream()
                val bitmap = BitmapFactory.decodeStream(input)
                withContext(Dispatchers.Main) {
                    profilePicture = bitmap
                    Log.d("PROFILE_DEBUG", "image loading success")
                }
            } catch (e: Exception) {
                Log.e("PROFILE_DEBUG", "image loading failure: ${e.message}")
                e.printStackTrace()
            }
        }
    }

    fun updateProfile(context: Context, phone: String, language: String, imageBitmap: Bitmap?, onResult: (Boolean, String) -> Unit) {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                val emailPart = (loggedInUser?.emailOrPhone ?: "").toRequestBody("text/plain".toMediaTypeOrNull())
                val phonePart = phone.toRequestBody("text/plain".toMediaTypeOrNull())
                val languagePart = language.toRequestBody("text/plain".toMediaTypeOrNull())
                
                var imagePart: MultipartBody.Part? = null
                if (imageBitmap != null) {
                    val file = File(context.cacheDir, "profile_upload.jpg")
                    val bos = ByteArrayOutputStream()
                    imageBitmap.compress(Bitmap.CompressFormat.JPEG, 90, bos)
                    val bitmapData = bos.toByteArray()

                    val fos = FileOutputStream(file)
                    fos.write(bitmapData)
                    fos.flush()
                    fos.close()

                    val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                    // Field name MUST be profile_image as per backend requirement
                    imagePart = MultipartBody.Part.createFormData("profile_image", file.name, requestFile)
                }

                val response = RetrofitClient.apiService.updateProfile(emailPart, phonePart, languagePart, imagePart)
                if (response.isSuccessful) {
                    // Refresh profile data and image after successful update
                    fetchProfile(loggedInUser?.emailOrPhone ?: "")
                    onResult(true, response.body()?.message ?: "Profile updated successfully")
                } else {
                    val errorMsg = parseError(response.errorBody()?.string())
                    errorMessage = errorMsg
                    onResult(false, errorMsg)
                }
            } catch (e: Exception) {
                errorMessage = "Error: ${e.message}"
                onResult(false, errorMessage!!)
            } finally {
                isLoading = false
            }
        }
    }

    fun fetchHistory(email: String) {
        Log.d("FormViewModel", "fetchHistory REQUEST URL: ${RetrofitClient.BASE_URL}history/$email")
        if (email.isEmpty()) {
            Log.e("FormViewModel", "fetchHistory aborted: email is empty")
            return
        }
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val response = RetrofitClient.apiService.getHistory(email)
                if (response.isSuccessful) {
                    val historyResp = response.body()
                    val historyList = historyResp?.history ?: emptyList()
                    
                    Log.d("FormViewModel", "fetchHistory RESPONSE BODY: $historyResp")
                    Log.d("FormViewModel", "fetchHistory SUCCESS: PARSED SIZE = ${historyList.size}")

                    withContext(Dispatchers.Main) {
                        formsHistory.clear()
                        formsHistory.addAll(historyList)
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("FormViewModel", "fetchHistory FAILED: CODE=${response.code()} MSG=${response.message()} ERROR_BODY=$errorBody")
                }
            } catch (e: Exception) {
                Log.e("FormViewModel", "fetchHistory EXCEPTION: ${e.message}", e)
            }
        }
    }


    fun deleteHistory(
        docId: Int,
        onResult: (Boolean, String) -> Unit
    ) {

        viewModelScope.launch {

            try {

                val response =
                    RetrofitClient.apiService.deleteHistory(docId)

                if (response.isSuccessful) {

                    formsHistory.removeAll {
                        it.id == docId
                    }

                    onResult(
                        true,
                        response.body()?.message
                            ?: "History deleted successfully"
                    )

                } else {

                    onResult(
                        false,
                        "Failed to delete history"
                    )
                }

            } catch (e: Exception) {

                onResult(
                    false,
                    e.message ?: "Error"
                )
            }
        }
    }
    fun fetchFormDetails(docId: Int, onResult: (Boolean) -> Unit) {
        viewModelScope.launch(Dispatchers.IO) {
            withContext(Dispatchers.Main) {
                isLoading = true
                selectedFormDetails = null
                selectedFormBitmap = null
            }
            try {
                val response = RetrofitClient.apiService.getFormDetails(docId)
                if (response.isSuccessful) {
                    val details = response.body()
                    withContext(Dispatchers.Main) {
                        selectedFormDetails = details
                    }
                    if (details?.fileUrl != null) {
                        loadFormImage(details.fileUrl)
                    }
                    withContext(Dispatchers.Main) {
                        onResult(true)
                    }
                } else {
                    withContext(Dispatchers.Main) {
                        onResult(false)
                    }
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    onResult(false)
                }
            } finally {
                withContext(Dispatchers.Main) {
                    isLoading = false
                }
            }
        }
    }

    private fun loadFormImage(imageUrl: String) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val fullUrl = UrlHelper.cleanUrl(imageUrl)
                val cacheBusterUrl = fullUrl + if (fullUrl.contains("?")) "&t=${System.currentTimeMillis()}" else "?t=${System.currentTimeMillis()}"
                val url = URL(cacheBusterUrl)
                val connection = url.openConnection()
                connection.useCaches = false
                connection.doInput = true
                connection.connect()
                val input = connection.getInputStream()
                val bitmap = BitmapFactory.decodeStream(input)
                withContext(Dispatchers.Main) {
                    selectedFormBitmap = bitmap
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    fun submitFeedback(
        experience: String,
        voiceHelp: String,
        recommend: String,
        comments: String,
        onResult: (Boolean, String) -> Unit
    ) {
        val email = loggedInUser?.emailOrPhone ?: "guest@example.com"
        viewModelScope.launch {
            isLoading = true
            try {
                val request = FeedbackRequest(
                    userEmail = email,
                    appExperience = experience,
                    voiceGuidanceHelpful = voiceHelp,
                    recommendApp = recommend,
                    additionalComments = comments
                )
                val response = RetrofitClient.apiService.submitFeedback(request)
                if (response.isSuccessful) {
                    onResult(true, response.body()?.message ?: "Feedback submitted")
                } else {
                    onResult(false, "Failed to submit feedback")
                }
            } catch (e: Exception) {
                onResult(false, "Error: ${e.message}")
            } finally {
                isLoading = false
            }
        }
    }

    fun playHistoryAudio(audioPath: String?) {
        Log.d("AUDIO_DEBUG", "audio URL received: $audioPath")
        if (audioPath.isNullOrEmpty()) {
            Log.e("AUDIO_DEBUG", "audio playback failed: path is null or empty")
            return
        }
        
        val fullUrl = UrlHelper.cleanUrl(audioPath)
        Log.d("AUDIO_DEBUG", "Final Audio URL: $fullUrl")

        exoPlayer?.let { player ->
            try {
                player.stop()
                player.clearMediaItems()
                val mediaItem = MediaItem.fromUri(fullUrl)
                player.setMediaItem(mediaItem)
                player.prepare()
                player.play()
                currentlyPlayingUrl = audioPath
                isAudioPlaying = true
                Log.d("AUDIO_DEBUG", "audio playback started")
            } catch (e: Exception) {
                Log.e("AUDIO_DEBUG", "audio playback failed: ${e.message}")
            }
        }
    }

    private suspend fun uploadDocumentInternal(
        context: Context,
        bitmap: Bitmap,
        email: String,
        language: String
    ): Boolean {

        return withContext(Dispatchers.IO) {

            try {
                withContext(Dispatchers.Main) {
                    isBackendProcessing = true
                }
                Log.d("PDF_DEBUG", "PDF generation started")

                val file =
                    File(
                        context.cacheDir,
                        "scan_upload.jpg"
                    )

                val bos = ByteArrayOutputStream()

                bitmap.compress(
                    Bitmap.CompressFormat.JPEG,
                    90,
                    bos
                )

                val fos =
                    FileOutputStream(file)

                fos.write(
                    bos.toByteArray()
                )

                fos.flush()

                fos.close()

                val requestFile =
                    file.asRequestBody(
                        "image/jpeg"
                            .toMediaTypeOrNull()
                    )

                val body =
                    MultipartBody.Part.createFormData(
                        "file",
                        file.name,
                        requestFile
                    )

                val userEmailPart =
                    email.toRequestBody(
                        "text/plain"
                            .toMediaTypeOrNull()
                    )

                val languagePart =
                    language.toRequestBody(
                        "text/plain"
                            .toMediaTypeOrNull()
                    )

                val response =
                    RetrofitClient.apiService.uploadDocument(
                        userEmailPart,
                        languagePart,
                        body
                    )

                withContext(Dispatchers.Main) {

                    if (response.isSuccessful) {

                        val uploadResponse =
                            response.body()

                        backendGuidance =
                            uploadResponse?.guidance ?: ""

                        val audioPath =
                            uploadResponse?.audioFile

                        if (!audioPath.isNullOrEmpty()) {
                            backendAudioUrl = UrlHelper.cleanUrl(audioPath)
                        }

                        val pdfPath = uploadResponse?.pdfFile
                        Log.d(
                            "PDF_DEBUG",
                            "RAW PDF PATH = $pdfPath"
                        )

                        if (!pdfPath.isNullOrEmpty()) {
                            backendPdfUrl = UrlHelper.cleanUrl(pdfPath)
                            Log.d(
                                "PDF_DEBUG",
                                "FINAL PDF URL = $backendPdfUrl"
                            )
                        } else {
                            backendPdfUrl = backendAudioUrl?.replace("audio", "pdf")?.replace("audio_files", "pdf_files")?.replace(".mp3", ".pdf")
                            Log.d(
                                "PDF_DEBUG",
                                "PDF URL IS NULL, using fallback: $backendPdfUrl"
                            )
                        }

                        fetchHistory(email)

                        withContext(Dispatchers.Main) {
                            isBackendProcessing = false
                            Log.d("PDF_DEBUG", "PDF generation completed")
                            android.widget.Toast.makeText(context, "PDF Ready for Download", android.widget.Toast.LENGTH_SHORT).show()
                            Log.d("PDF_DEBUG", "Download URL received")
                        }

                        true

                    } else {

                        Log.e(
                            "PDF_DEBUG",
                            "UPLOAD FAILED = ${response.code()}"
                        )
                        
                        withContext(Dispatchers.Main) {
                            isBackendProcessing = false
                        }

                        false
                    }
                }

            } catch (e: Exception) {

                Log.e(
                    "PDF_DEBUG",
                    "UPLOAD ERROR = ${e.message}"
                )
                
                withContext(Dispatchers.Main) {
                    isBackendProcessing = false
                }

                false
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
        formsHistory.clear()
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
        exoPlayer?.setPlaybackSpeed(rate)
    }

    fun analyzeImage(context: Context, bitmap: Bitmap, language: Language?, onResult: () -> Unit) {
        isLoading = true 
        errorMessage = null
        capturedBitmap = bitmap
        
        val langCode = language?.code ?: "en"
        val analyzer = FormAnalyzer(context)

        // Using Local FormAnalyzer to restore stable OCR and field detection
        analyzer.analyzeForm(bitmap, langCode) { result ->
            result.fold(
                onSuccess = { analysis ->
                    detectedFormType = analysis.formType
                    detectedAccountType = analysis.bankType
                    detectedConfidence = analysis.confidence
                    detectedFields = analysis.fields
                    
                    if (recentScans.none { it.bitmap == bitmap }) {
                        recentScans.add(0, RecentScan(detectedFormType, bitmap, detectedFormType, detectedFields))
                    }

                    // Background upload to keep backend features intact
                    viewModelScope.launch {
                        uploadDocumentInternal(context, bitmap, loggedInUser?.emailOrPhone ?: "guest@example.com", langCode)
                    }
                    
                    isLoading = false
                    onResult()
                },
                onFailure = { e ->
                    errorMessage = e.message ?: "Analysis failed"
                    isLoading = false
                    onResult()
                }
            )
        }
    }

    fun playBackendAudio() {
        backendAudioUrl?.let { url ->
            exoPlayer?.let { player ->
                player.stop()
                player.clearMediaItems()
                val mediaItem = MediaItem.fromUri(url)
                player.setMediaItem(mediaItem)
                player.prepare()
                player.play()
                currentlyPlayingUrl = url
            }
        }
    }

    fun stopAudio() {
        exoPlayer?.stop()
        ttsHelper?.stop()
        isAudioPlaying = false
        currentlyPlayingUrl = null
    }

    fun downloadSpecificPdf(context: Context, url: String?) {
        Log.d("PDF_DEBUG", "download URL received: $url")

        if (url.isNullOrEmpty()) {
            android.widget.Toast.makeText(context, "PDF not available", android.widget.Toast.LENGTH_SHORT).show()
            return
        }

        val fullUrl = UrlHelper.cleanUrl(url)

        android.widget.Toast.makeText(context, "Downloading PDF...", android.widget.Toast.LENGTH_SHORT).show()
        Log.d("PDF_DEBUG", "download started")

        viewModelScope.launch(Dispatchers.IO) {
            try {
                val connection = URL(fullUrl).openConnection()
                connection.doInput = true
                connection.connect()

                val inputStream = connection.getInputStream()
                
                val filename = "FormSahayak_Report_${System.currentTimeMillis()}.pdf"
                
                val resolver = context.contentResolver
                val contentValues = android.content.ContentValues().apply {
                    put(MediaStore.MediaColumns.DISPLAY_NAME, filename)
                    put(MediaStore.MediaColumns.MIME_TYPE, "application/pdf")
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                        put(MediaStore.MediaColumns.RELATIVE_PATH, android.os.Environment.DIRECTORY_DOWNLOADS + "/FormSahayak")
                    }
                }
                
                val collection = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                    MediaStore.Downloads.EXTERNAL_CONTENT_URI
                } else {
                    null
                }

                if (collection != null) {
                    val uri = resolver.insert(collection, contentValues)
                    if (uri != null) {
                        resolver.openOutputStream(uri)?.use { outputStream ->
                            inputStream.copyTo(outputStream)
                        }
                        withContext(Dispatchers.Main) {
                            Log.d("PDF_DEBUG", "download completed")
                            android.widget.Toast.makeText(context, "PDF Downloaded Successfully", android.widget.Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        throw Exception("Failed to create MediaStore entry")
                    }
                } else {
                    @Suppress("DEPRECATION")
                    val dir = File(android.os.Environment.getExternalStoragePublicDirectory(android.os.Environment.DIRECTORY_DOWNLOADS), "FormSahayak")
                    if (!dir.exists()) dir.mkdirs()
                    val file = File(dir, filename)
                    java.io.FileOutputStream(file).use { outputStream ->
                        inputStream.copyTo(outputStream)
                    }
                    withContext(Dispatchers.Main) {
                        Log.d("PDF_DEBUG", "download completed")
                        android.widget.Toast.makeText(context, "PDF Downloaded Successfully", android.widget.Toast.LENGTH_SHORT).show()
                    }
                }

            } catch (e: Exception) {
                Log.e("PDF_DEBUG", "DOWNLOAD ERROR = ${e.message}")
                Log.d("PDF_DEBUG", "download failed")
                withContext(Dispatchers.Main) {
                    android.widget.Toast.makeText(context, "Download failed: ${e.message}", android.widget.Toast.LENGTH_LONG).show()
                }
            }
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
        backendAudioUrl = null
        errorMessage = null
        stopAudio()
    }

    override fun onCleared() {
        super.onCleared()
        ttsHelper?.shutdown()
        exoPlayer?.release()
    }
}
