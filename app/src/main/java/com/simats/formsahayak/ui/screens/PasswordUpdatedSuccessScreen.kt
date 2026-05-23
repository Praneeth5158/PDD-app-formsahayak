package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Login
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun PasswordUpdatedSuccessScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onBackToLogin: () -> Unit
) {
    val isDark = isDarkMode || isHighContrast

    val backgroundBrush = if (isHighContrast) {
        Brush.verticalGradient(listOf(Color.Black, Color.Black))
    } else if (isDarkMode) {
        Brush.verticalGradient(listOf(Color(0xFF121212), Color(0xFF121212)))
    } else {
        Brush.verticalGradient(listOf(Color(0xFFE0F7FA), Color(0xFFF0FBFF)))
    }

    val cardColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isHighContrast) Color.Yellow else if (isDarkMode) Color.White else Color(0xFF1A237E)

    // Translations
    val title = when (selectedLanguage?.code) {
        "te" -> "పాస్‌వర్డ్ విజయవంతంగా రీసెట్ చేయబడింది!"
        "ta" -> "கடவுச்சொல் வெற்றிகரமாக மீட்டமைக்கப்பட்டது!"
        "hi" -> "पासवर्ड सफलतापूर्वक\nरीसेट हो गया!"
        else -> "Password Reset\nSuccessfully!"
    }
    val subtitle = when (selectedLanguage?.code) {
        "te" -> "మీ పాస్‌వర్డ్ మార్చబడింది"
        "ta" -> "உங்கள் கடவுச்சொல் மாற்றப்பட்டுள்ளது"
        "hi" -> "आपका पासवर्ड बदल दिया गया है"
        else -> "Your password has been changed"
    }
    val infoText = when (selectedLanguage?.code) {
        "te" -> "మీరు ఇప్పుడు మీ కొత్త పాస్‌వర్డ్‌తో లాగిన్ చేయవచ్చు"
        "ta" -> "நீங்கள் இப்போது உங்கள் புதிய கடவுச்சொல்லுடன் உள்நுழையலாம்"
        "hi" -> "अब आप अपने नए\nपासवर्ड के साथ लॉगिन कर सकते हैं"
        else -> "You can now login with your new\npassword"
    }
    val buttonText = when (selectedLanguage?.code) {
        "te" -> "లాగిన్‌కి తిరిగి వెళ్లండి"
        "ta" -> "உள்நுழைவுக்குத் திரும்பு"
        "hi" -> "लॉगिन पर वापस जाएं"
        else -> "Back to Login"
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(32.dp),
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = cardColor),
            elevation = CardDefaults.cardElevation(defaultElevation = if (isDark) 0.dp else 8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Success Icon
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = if (isHighContrast) Color.Yellow else Color(0xFFE8F5E9)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Surface(
                            modifier = Modifier.size(50.dp),
                            shape = CircleShape,
                            color = Color.Transparent,
                            border = androidx.compose.foundation.BorderStroke(4.dp, if (isHighContrast) Color.Black else Color(0xFF4CAF50))
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.Check,
                                    contentDescription = "Success",
                                    tint = if (isHighContrast) Color.Black else Color(0xFF4CAF50),
                                    modifier = Modifier.size(32.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = title,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = if (isDark) Color.LightGray else Color.Gray,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Info Box
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF2C2C2C) else Color(0xFFE3F2FD),
                    border = if (isHighContrast) androidx.compose.foundation.BorderStroke(1.dp, Color.White) else null
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Lock,
                            contentDescription = null,
                            tint = if (isHighContrast) Color.Yellow else Color(0xFF2196F3),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = infoText,
                            color = if (isHighContrast) Color.White else if (isDarkMode) Color.White else Color(0xFF1565C0),
                            fontSize = 12.sp,
                            textAlign = TextAlign.Center
                        )
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onBackToLogin,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isHighContrast) Color.Yellow else Color(0xFF2196F3),
                        contentColor = if (isHighContrast) Color.Black else Color.White
                    )
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.Login,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = buttonText,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
