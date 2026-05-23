package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Folder
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun AllPermissionsSetScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onContinue: () -> Unit
) {
    val backgroundColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF121212) else Color(0xFFF8FBFF)
    val textColor = if (isHighContrast) Color.Yellow else if (isDarkMode) Color.White else Color.Black
    val subTextColor = if (isHighContrast) Color.White else if (isDarkMode) Color.LightGray else Color.Gray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        // Success Icon
        Surface(
            modifier = Modifier.size(100.dp),
            shape = CircleShape,
            color = if (isHighContrast) Color.Yellow else Color(0xFFE8F5E9)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (isHighContrast) Color.Black else Color(0xFF4CAF50),
                    modifier = Modifier.size(60.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        val titleText = when (selectedLanguage?.code) {
            "te" -> "అన్నీ సిద్ధం! 🎉"
            "ta" -> "எல்லாம் தயார்! 🎉"
        "hi" -> "सभी अनुमतियाँ सेट हो गईं!"
        else -> "All Permissions Set!"
        }
        Text(
            text = titleText,
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = TextAlign.Center
        )

        val subTitleText = when (selectedLanguage?.code) {
            "te" -> "అనుమతులు విజయవంతంగా పొందబడ్డాయి"
            "ta" -> "அனுமதிகள் வெற்றிகரமாகப் பெறப்பட்டன"
        "hi" -> "सभी अनुमतियाँ सफलतापूर्वक दी गईं"
        else -> "All Permissions Granted Successfully"
        }
        Text(
            text = subTitleText,
            fontSize = 16.sp,
            color = subTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(48.dp))

        // Summary Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            color = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF1E1E1E) else Color.White,
            border = androidx.compose.foundation.BorderStroke(1.dp, if (isHighContrast) Color.White else Color(0xFFEEEEEE)),
            shadowElevation = if (isHighContrast) 0.dp else 2.dp
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                PermissionSummaryItem(Icons.Default.PhotoCamera, when(selectedLanguage?.code) {"hi" -> "कैमरा"; "te" -> "కెమెరా"; "ta" -> "கேமரா"; else -> "Camera"}, textColor)
                Spacer(modifier = Modifier.height(16.dp))
                PermissionSummaryItem(Icons.Default.Folder, when(selectedLanguage?.code) {"hi" -> "स्टोरेज"; "te" -> "స్టోరేజ్"; "ta" -> "சேமிப்பகம்"; else -> "Storage"}, textColor)
                Spacer(modifier = Modifier.height(16.dp))
                PermissionSummaryItem(Icons.Default.Mic, when(selectedLanguage?.code) {"hi" -> "माइक्रोफ़ोन"; "te" -> "మైక్రోఫోన్"; "ta" -> "மைக்ரோஃபோன்"; else -> "Microphone"}, textColor)
            }
        }

        Spacer(modifier = Modifier.weight(1f))

        Button(
            onClick = onContinue,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isHighContrast) Color.Yellow else Color(0xFF00C853),
                contentColor = if (isHighContrast) Color.Black else Color.White
            )
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                val btnText = when (selectedLanguage?.code) {
                    "te" -> "డ్యాష్‌బోర్డ్‌కు కొనసాగండి"
                    "ta" -> "டாஷ்போர்டுக்குத் தொடரவும்"
        "hi" -> "डैशबोर्ड पर जारी रखें"
        else -> "Continue to Dashboard"
                }
                Text(text = btnText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                Spacer(modifier = Modifier.width(8.dp))
                Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null, modifier = Modifier.size(20.dp))
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun PermissionSummaryItem(icon: ImageVector, title: String, textColor: Color) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Icon(imageVector = icon, contentDescription = null, tint = Color(0xFF2196F3), modifier = Modifier.size(24.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium, color = textColor, modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(20.dp))
    }
}
