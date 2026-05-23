package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun VerifyingFormScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onVerificationComplete: () -> Unit
) {
    val isDark = isDarkMode || isHighContrast
    val backgroundColor = if (isDark) Color.Black else Color(0xFFF8FBFF)
    val textColor = if (isDark) Color.White else Color.Black

    // Translation logic
    val title = when (selectedLanguage?.code) {
        "te" -> "ఫారమ్‌ను ధృవీకరిస్తోంది"
        "ta" -> "படிவத்தைச் சரிபார்க்கிறது"
        "hi" -> "फॉर्म सत्यापित हो रहा है"
        else -> "Verifying Form"
    }
    val pleaseWaitText = when (selectedLanguage?.code) {
        "te" -> "దయచేసి వేచి ఉండండి..."
        "ta" -> "தயவுசெய்து காத்திருங்கள்..."
        "hi" -> "कृपया प्रतीक्षा करें..."
        else -> "Please wait..."
    }
    val step1 = when (selectedLanguage?.code) {
        "te" -> "పూర్తి పేరు"
        "ta" -> "முழு பெயர்"
        "hi" -> "पूरा नाम"
        else -> "Full Name"
    }
    val step2 = when (selectedLanguage?.code) {
        "te" -> "ఖాతా సంఖ్య"
        "ta" -> "கணக்கு எண்"
        "hi" -> "खाता संख्या"
        else -> "Account Number"
    }
    val step3 = when (selectedLanguage?.code) {
        "te" -> "చిరునామా"
        "ta" -> "முகவரி"
        "hi" -> "पता"
        else -> "Address"
    }
    val step4 = when (selectedLanguage?.code) {
        "te" -> "సంతకం"
        "ta" -> "கையெழுத்து"
        "hi" -> "हस्ताक्षर"
        else -> "Signature"
    }

    var progress by remember { mutableStateOf(0f) }

    LaunchedEffect(Unit) {
        while (progress < 1f) {
            delay(50)
            progress += 0.02f
        }
        onVerificationComplete()
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(contentAlignment = Alignment.Center) {
            CircularProgressIndicator(
                progress = { progress },
                modifier = Modifier.size(150.dp),
                color = Color(0xFF2196F3),
                strokeWidth = 8.dp,
                trackColor = if (isDark) Color(0xFF333333) else Color(0xFFE3F2FD)
            )
            Text(
                text = "${(progress * 100).toInt()}%",
                fontSize = 24.sp,
                fontWeight = FontWeight.ExtraBold,
                color = Color(0xFF2196F3)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Text(
            text = title,
            fontSize = 28.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = pleaseWaitText,
            fontSize = 16.sp,
            color = if (isDark) Color.LightGray else Color.Gray
        )

        Spacer(modifier = Modifier.height(48.dp))

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            VerificationStep(text = step1, isDone = progress > 0.3f, isDark = isDark)
            VerificationStep(text = step2, isDone = progress > 0.5f, isDark = isDark)
            VerificationStep(text = step3, isDone = progress > 0.7f, isDark = isDark)
            VerificationStep(text = step4, isDone = progress > 0.9f, isDark = isDark)
        }
    }
}

@Composable
fun VerificationStep(text: String, isDone: Boolean, isDark: Boolean) {
    Row(
        modifier = Modifier.fillMaxWidth(0.7f),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(
            text = text, 
            color = if (isDone) (if (isDark) Color.White else Color.Black) else Color.Gray, 
            fontWeight = if (isDone) FontWeight.Bold else FontWeight.Normal
        )
        if (isDone) {
            Text("✔", color = Color(0xFF4CAF50), fontWeight = FontWeight.Bold)
        } else {
            Text("○", color = Color.Gray)
        }
    }
}
