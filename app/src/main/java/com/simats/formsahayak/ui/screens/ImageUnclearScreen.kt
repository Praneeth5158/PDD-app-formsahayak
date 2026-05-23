package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ImageUnclearScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onTryAgain: () -> Unit,
    onBackToHome: () -> Unit
) {
    val backgroundColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF121212) else Color(0xFFF8FBFF)
    val cardColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkMode || isHighContrast) Color.White else Color(0xFF2C3E50)
    val secondaryTextColor = if (isDarkMode || isHighContrast) Color.LightGray else Color.Gray

    // Translation logic
    val title = when (selectedLanguage?.code) {
        "te" -> "చిత్రం అస్పష్టంగా ఉంది"
        "ta" -> "படம் தெளிவாக இல்லை"
        "hi" -> "छवि अस्पष्ट है"
        else -> "Image is Unclear"
    }
    val subtitle = when (selectedLanguage?.code) {
        "te" -> "అప్‌లోడ్ చేసిన చిత్రం మసకగా లేదా అస్పష్టంగా ఉంది. దయచేసి ఫారమ్ యొక్క స్పష్టమైన ఫోటోను అప్‌లోడ్ చేయండి."
        "ta" -> "பதிவேற்றப்பட்ட படம் மங்கலாக அல்லது தெளிவாக இல்லை. படிவத்தின் தெளிவான புகைப்படத்தைப் பதிவேற்றவும்."
        "hi" -> "अपलोड की गई छवि धुंधली या अस्पष्ट है। कृपया फॉर्म की स्पष्ट फोटो अपलोड करें।"
        else -> "The uploaded image is blurry or unclear. Please upload a clear photo of the form."
    }
    val tipsTitle = when (selectedLanguage?.code) {
        "te" -> "మెరుగైన చిత్రాల కోసం చిట్కాలు:"
        "ta" -> "சிறந்த படங்களுக்கான உதவிக்குறிப்புகள்:"
        "hi" -> "बेहतर छवियों के लिए टिप्स:"
        else -> "Tips for better images:"
    }
    val tip1 = when (selectedLanguage?.code) {
        "te" -> "ఫారమ్ బాగా వెలిగి ఉండేలా చూసుకోండి"
        "ta" -> "படிவம் வெளிச்சமாக இருப்பதை உறுதி செய்யவும்"
        "hi" -> "सुनिश्चित करें कि फॉर्म पर पर्याप्त रोशनी है"
        else -> "Make sure the form is well-lit"
    }
    val tip2 = when (selectedLanguage?.code) {
        "te" -> "మీ ఫోన్‌ను నిలకడగా పట్టుకోండి"
        "ta" -> "உங்கள் தொலைபேசியை நிலையாக வைத்திருக்கவும்"
        "hi" -> "अपने फ़ोन को स्थिर रखें"
        else -> "Hold your phone steady"
    }
    val tip3 = when (selectedLanguage?.code) {
        "te" -> "అన్ని ఫీల్డ్‌లు కనిపిస్తున్నాయని నిర్ధారించుకోండి"
        "ta" -> "அனைத்து புலங்களும் தெரிவதை உறுதி செய்யவும்"
        "hi" -> "सुनिश्चित करें कि सभी फ़ील्ड दिखाई दे रहे हैं"
        else -> "Ensure all fields are visible"
    }
    val tip4 = when (selectedLanguage?.code) {
        "te" -> "ఫారమ్‌పై నీడలు పడకుండా చూసుకోండి"
        "ta" -> "படிவத்தில் நிழல்கள் விழுவதைத் தவிர்க்கவும்"
        "hi" -> "फॉर्म पर परछाई से बचें"
        else -> "Avoid shadows on the form"
    }
    val tryAgainText = when (selectedLanguage?.code) {
        "te" -> "మళ్లీ ప్రయత్నించండి"
        "ta" -> "மீண்டும் முயற்சிக்கவும்"
        "hi" -> "पुनः प्रयास करें"
        else -> "Try Again"
    }
    val backToHomeText = when (selectedLanguage?.code) {
        "te" -> "హోమ్‌కి తిరిగి వెళ్ళు"
        "ta" -> "முகப்புக்குத் திரும்பு"
        "hi" -> "होम पर वापस जाएं"
        else -> "Back to Home"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            color = cardColor,
            shadowElevation = if (isHighContrast) 0.dp else 8.dp,
            border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Error Icon
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = if (isDarkMode || isHighContrast) Color(0xFF310000) else Color(0xFFFFEBEE)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = Color(0xFFEF5350),
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isHighContrast) Color.Yellow else textColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = subtitle,
                    fontSize = 15.sp,
                    color = secondaryTextColor,
                    textAlign = TextAlign.Center,
                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Tips Section
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    color = if (isDarkMode || isHighContrast) Color.Black else Color(0xFFF1F4F9),
                    shape = RoundedCornerShape(12.dp),
                    border = if (isHighContrast) androidx.compose.foundation.BorderStroke(1.dp, Color.White) else null
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = tipsTitle,
                            fontWeight = FontWeight.Bold,
                            fontSize = 14.sp,
                            color = textColor
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        
                        val tips = listOf(tip1, tip2, tip3, tip4)
                        
                        tips.forEach { tip ->
                            Row(
                                modifier = Modifier.padding(vertical = 4.dp),
                                verticalAlignment = Alignment.Top
                            ) {
                                Text("• ", color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)
                                Text(
                                    text = tip,
                                    fontSize = 13.sp,
                                    color = secondaryTextColor
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onTryAgain,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isHighContrast) Color.Yellow else Color(0xFF2196F3),
                        contentColor = if (isHighContrast) Color.Black else Color.White
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.PhotoCamera, contentDescription = null, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(tryAgainText, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onBackToHome,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        backToHomeText,
                        color = if (isHighContrast) Color.White else secondaryTextColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
