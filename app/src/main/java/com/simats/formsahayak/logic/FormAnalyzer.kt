package com.simats.formsahayak.logic

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Rect
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.text.TextRecognition
import com.google.mlkit.vision.text.Text
import com.google.mlkit.vision.text.latin.TextRecognizerOptions
import java.util.regex.Pattern

data class DetectedField(
    val id: Int,
    val name: String,
    val bounds: Rect,
    val instruction: String,
    val isFilled: Boolean = false,
    val confidence: Float = 1.0f
)

class FormAnalyzer(private val context: Context) {
    private val recognizer = TextRecognition.getClient(TextRecognizerOptions.DEFAULT_OPTIONS)

    // Comprehensive Keyword Mapping for Field Categorization
    private val fieldCategoryMap = mapOf(
        "Name" to listOf("name", "full name", "first name", "last name", "surname", "applicant name", "పేరు", "பெயர்", "नाम"),
        "Phone" to listOf("phone", "mobile", "ph", "ph no", "contact", "tel", "telephone", "cell", "ఫోన్", "தொலைபேசி", "फोन", "मोबाइल"),
        "Email" to listOf("email", "e-mail", "email address", "eml", "ఈమెయిల్", "மின்னஞ்சல்", "ईमेल"),
        "DOB" to listOf("dob", "date of birth", "birth date", "d.o.b", "పుట్టిన తేదీ", "பிறந்த தேதி", "जन्म तिथि"),
        "Address" to listOf("address", "residence", "residential address", "addr", "permanent address", "చిరునామా", "முகவரி", "पता"),
        "ID Type" to listOf("id type", "identification type", "identity", "document type", "पहचान प्रकार"),
        "ID Number" to listOf("id number", "id no", "identification no", "aadhar", "pan", "passport", "voter id", "पहचान संख्या"),
        "Account Type" to listOf("account type", "savings", "checking", "current", "type of account", "a/c type", "खाता प्रकार"),
        "Account Number" to listOf("account number", "account no", "acc no", "a/c no", "acct no", "ఖాతా సంఖ్య", "கணக்கு எண்", "खाता संख्या"),
        "Deposit" to listOf("initial deposit", "deposit", "deposit amount", "amount to deposit", "మొత్తం", "தொகை", "जमा राशि"),
        "Loan Type" to listOf("loan type", "type of loan", "loan category", "ऋण प्रकार"),
        "Loan Amount" to listOf("loan amount", "requested amount", "loan sum", "principal", "ऋण राशि"),
        "Loan Purpose" to listOf("loan purpose", "purpose of loan", "reason for loan", "ऋण का उद्देश्य"),
        "Employer" to listOf("employer", "company", "organization", "employer name", "नियोक्ता"),
        "Job Title" to listOf("job title", "occupation", "profession", "designation", "vocation", "వృత్తి", "தொழில்", "व्यवसाय"),
        "Income" to listOf("income", "monthly income", "annual income", "salary", "gross income", "आय"),
        "Signature" to listOf("signature", "sign here", "applicant signature", "sign", "సంతకం", "கையெழுத்து", "हस्ताक्षर"),
        "Date" to listOf("date", "dated", "today's date", "తేదీ", "தேதி", "तारीख", "दिनांक"),
        "Branch" to listOf("branch", "branch name", "office", "bank branch", "శాఖ", "கிளை", "शाखा"),
        "IFSC" to listOf("ifsc", "ifsc code", "bank ifsc")
    )

    private val commonBanks = listOf(
        "State Bank of India", "SBI", "HDFC Bank", "ICICI Bank", "Axis Bank", 
        "Punjab National Bank", "PNB", "Bank of Baroda", "Canara Bank", 
        "Union Bank of India", "IndusInd Bank", "IDBI Bank", "Yes Bank", "Indian Bank"
    )

    // Localized Instruction Map
    private val localizedLabels = mapOf(
        "te" to mapOf(
            "Name" to "పేరు", "Phone" to "ఫోన్ నంబర్", "Email" to "ఈమెయిల్",
            "DOB" to "పుట్టిన తేదీ", "Address" to "చిరునామా", "ID Type" to "గుర్తింపు రకం",
            "ID Number" to "గుర్తింపు నంబర్", "Account Type" to "ఖాతా రకం", 
            "Account Number" to "ఖాతా సంఖ్య", "Deposit" to "డిపాజిట్", 
            "Loan Type" to "రుణ రకం", "Loan Amount" to "రుణం మొత్తం",
            "Loan Purpose" to "రుణ ఉద్దేశ్యం", "Employer" to "యజమాని",
            "Job Title" to "వృత్తి", "Income" to "ఆదాయం", 
            "Signature" to "సంతకం", "Date" to "తేదీ",
            "Branch" to "శాఖ", "IFSC" to "IFSC కోడ్"
        ),
        "ta" to mapOf(
            "Name" to "பெயர்", "Phone" to "தொலைபேசி", "Email" to "மின்னஞ்சல்",
            "DOB" to "பிறந்த தேதி", "Address" to "முகவரி", "ID Type" to "அடையாள வகை",
            "ID Number" to "அடையாள எண்", "Account Type" to "கணக்கு வகை",
            "Account Number" to "கணக்கு எண்", "Deposit" to "வைப்பு",
            "Loan Type" to "கடன் வகை", "Loan Amount" to "கடன் தொகை",
            "Loan Purpose" to "கடன் நோக்கம்", "Employer" to "பணியமர்த்துபவர்",
            "Job Title" to "தொழில்", "Income" to "வருமானம்",
            "Signature" to "கையெழுத்து", "Date" to "தேதி",
            "Branch" to "கிளை", "IFSC" to "IFSC குறியீடு"
        ),
        "hi" to mapOf(
            "Name" to "नाम", "Phone" to "फ़ोन नंबर", "Email" to "ईमेल",
            "DOB" to "जन्म तिथि", "Address" to "पता", "ID Type" to "पहचान प्रकार",
            "ID Number" to "पहचान संख्या", "Account Type" to "खाता प्रकार",
            "Account Number" to "खाता संख्या", "Deposit" to "जमा राशि",
            "Loan Type" to "ऋण प्रकार", "Loan Amount" to "ऋण राशि",
            "Loan Purpose" to "ऋण का उद्देश्य", "Employer" to "नियोक्ता",
            "Job Title" to "व्यवसाय", "Income" to "आय",
            "Signature" to "हस्ताक्षर", "Date" to "दिनांक",
            "Branch" to "शाखा", "IFSC" to "IFSC कोड"
        )
    )

    fun analyzeForm(bitmap: Bitmap, languageCode: String, onResult: (Result<AnalysisResult>) -> Unit) {
        val image = InputImage.fromBitmap(bitmap, 0)
        recognizer.process(image)
            .addOnSuccessListener { visionText ->
                val detectedFields = extractAllFields(visionText, bitmap, languageCode)
                
                if (detectedFields.isEmpty()) {
                    onResult(Result.failure(Exception("No input fields detected. Ensure the form is clear and well-lit.")))
                } else {
                    val heading = detectFormHeading(visionText, bitmap)
                    val bankType = detectBankType(visionText)
                    val confidence = calculateConfidence(visionText, detectedFields)
                    onResult(Result.success(AnalysisResult(heading, detectedFields, bankType, confidence)))
                }
            }
            .addOnFailureListener { e ->
                onResult(Result.failure(e))
            }
    }

    private fun extractAllFields(visionText: Text, bitmap: Bitmap, languageCode: String): List<DetectedField> {
        val detectedFields = mutableListOf<DetectedField>()
        val allBlocks = visionText.textBlocks
        var fieldId = 1

        allBlocks.forEach { block ->
            val text = block.text.trim()
            val lowerText = text.lowercase()

            if (text.length > 50 || text.length < 2) return@forEach

            val category = detectCategory(lowerText)
            val isLikelyLabel = category != null || text.endsWith(":") || text.endsWith(".")

            if (isLikelyLabel) {
                val labelBounds = block.boundingBox ?: return@forEach
                val categoryName = category ?: text.removeSuffix(":").removeSuffix(".").trim()
                val displayName = localizedLabels[languageCode]?.get(categoryName) ?: categoryName
                val inputBounds = findBestInputArea(labelBounds, allBlocks, bitmap)
                val isFilled = checkIfAreaOccupied(inputBounds, allBlocks, block)

                val instruction = when (languageCode) {
                    "te" -> "దయచేసి ఇక్కడ మీ $displayName నింపండి."
                    "ta" -> "தயவுசெய்து உங்கள் $displayName-ஐ இங்கே நிரப்பவும்."
                    "hi" -> "कृपया अपना $displayName यहाँ भरें।"
                    else -> "Please fill your $displayName here."
                }

                detectedFields.add(
                    DetectedField(
                        id = fieldId++,
                        name = displayName,
                        bounds = inputBounds,
                        instruction = instruction,
                        isFilled = isFilled
                    )
                )
            }
        }
        return finalizeDetections(detectedFields)
    }

    private fun detectCategory(text: String): String? {
        for ((category, keywords) in fieldCategoryMap) {
            if (keywords.any { text.contains(it, ignoreCase = true) }) {
                return category
            }
        }
        return null
    }

    private fun findBestInputArea(labelBounds: Rect, allBlocks: List<Text.TextBlock>, bitmap: Bitmap): Rect {
        val gap = 10
        val defaultWidth = (bitmap.width * 0.4).toInt()
        val defaultHeight = 80

        val rightRect = Rect(
            labelBounds.right + gap,
            labelBounds.top - 5,
            (labelBounds.right + gap + defaultWidth).coerceAtMost(bitmap.width - gap),
            labelBounds.bottom + 5
        )

        val bottomRect = Rect(
            labelBounds.left,
            labelBounds.bottom + gap,
            (labelBounds.right + (bitmap.width * 0.1).toInt()).coerceAtMost(bitmap.width - gap),
            (labelBounds.bottom + gap + defaultHeight).coerceAtMost(bitmap.height - gap)
        )

        return if (labelBounds.right > bitmap.width * 0.7) {
            bottomRect
        } else {
            rightRect
        }
    }

    private fun checkIfAreaOccupied(area: Rect, allBlocks: List<Text.TextBlock>, labelBlock: Text.TextBlock): Boolean {
        return allBlocks.any { block ->
            if (block == labelBlock) return@any false
            val bounds = block.boundingBox ?: return@any false
            area.contains(bounds.centerX(), bounds.centerY())
        }
    }

    private fun finalizeDetections(fields: List<DetectedField>): List<DetectedField> {
        val sorted = fields.sortedWith(compareBy({ it.bounds.top }, { it.bounds.left }))
        val filtered = mutableListOf<DetectedField>()
        sorted.forEach { current ->
            val isDuplicate = filtered.any { existing ->
                val overlap = Rect()
                overlap.set(current.bounds)
                if (overlap.intersect(existing.bounds)) {
                    val overlapArea = overlap.width() * overlap.height()
                    val currentArea = current.bounds.width() * current.bounds.height()
                    overlapArea > currentArea * 0.5
                } else false
            }
            if (!isDuplicate) filtered.add(current)
        }
        return filtered
    }

    private fun detectFormHeading(visionText: Text, bitmap: Bitmap): String {
        val headers = visionText.textBlocks.filter { 
            val b = it.boundingBox ?: return@filter false
            b.top < bitmap.height / 4 
        }.sortedBy { it.boundingBox?.top ?: 0 }

        val bankMatch = visionText.textBlocks.find { block ->
            commonBanks.any { bank -> block.text.contains(bank, ignoreCase = true) }
        }

        val heading = headers.firstOrNull { it.text.length > 5 }?.text?.replace("\n", " ") ?: "Form"
        return if (bankMatch != null) "${bankMatch.text.trim()} - $heading" else heading
    }

    private fun detectBankType(visionText: Text): String {
        val text = visionText.text.lowercase()
        return when {
            text.contains("savings") || text.contains("saving") -> "Savings Account"
            text.contains("current") -> "Current Account"
            text.contains("fixed") || text.contains("term deposit") -> "Fixed Deposit"
            text.contains("loan") -> "Loan Application"
            text.contains("credit card") -> "Credit Card"
            else -> "General Form"
        }
    }

    private fun calculateConfidence(visionText: Text, fields: List<DetectedField>): Int {
        var score = 70 // Base score if text is recognized
        
        // Boost score based on number of fields found
        if (fields.size > 5) score += 10
        if (fields.size > 10) score += 10
        
        // Boost score if common bank keywords are found
        if (commonBanks.any { bank -> visionText.text.contains(bank, ignoreCase = true) }) {
            score += 5
        }
        
        // Penalty if text is very sparse
        if (visionText.text.length < 100) score -= 15
        
        return score.coerceIn(40, 99)
    }
}

data class AnalysisResult(
    val formType: String,
    val fields: List<DetectedField>,
    val bankType: String = "General",
    val confidence: Int = 85
)
