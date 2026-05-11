package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
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
fun VerificationSuccessScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onBackToLogin: () -> Unit,
    onGoToHome: () -> Unit
) {
    val isDark = isDarkMode || isHighContrast
    val backgroundColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF121212) else Color(0xFFF8FBFF)
    val cardColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryTextColor = if (isDark) Color.LightGray else Color.Gray

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Success Icon
        Box(contentAlignment = Alignment.Center) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = if (isDark) Color(0xFF1B5E20) else Color(0xFFE8F5E9)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        tint = Color(0xFF4CAF50),
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Verified\nSuccessfully! 🎉",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = if (isHighContrast) Color.Yellow else textColor
        )

        Text(
            text = "Your OTP has been verified",
            fontSize = 14.sp,
            color = secondaryTextColor,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Info Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = if (isDark) Color.Black else Color(0xFFF1F8E9),
            border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC8E6C9))
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                Icon(Icons.Default.VerifiedUser, contentDescription = null, tint = if (isHighContrast) Color.Yellow else Color(0xFF4CAF50))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Verification Complete", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = if (isHighContrast) Color.White else Color(0xFF2E7D32))
                    Text(
                        "Your phone number has been successfully verified. You can now access all features of FormSahayak.",
                        fontSize = 12.sp,
                        color = if (isHighContrast) Color.White else Color(0xFF43A047)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        SuccessItem(icon = Icons.Default.CheckCircle, text = "Your account is now active", color = if (isHighContrast) Color.Yellow else Color(0xFF2196F3), isHighContrast = isHighContrast)
        Spacer(modifier = Modifier.height(8.dp))
        SuccessItem(icon = Icons.Default.CheckCircle, text = "You can now login anytime", color = if (isHighContrast) Color.Yellow else Color(0xFF9C27B0), isHighContrast = isHighContrast)

        Spacer(modifier = Modifier.height(24.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = if (isDark) Color.Black else Color(0xFFFFFDE7),
            border = if (isHighContrast) androidx.compose.foundation.BorderStroke(1.dp, Color.White) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFF9C4))
        ) {
            Text(
                "Welcome to FormSahayak!\nLet's make form filling easier together",
                modifier = Modifier.padding(12.dp),
                textAlign = TextAlign.Center,
                fontSize = 12.sp,
                color = if (isHighContrast) Color.White else Color(0xFFF57F17),
                fontWeight = FontWeight.Medium
            )
        }

        Spacer(modifier = Modifier.height(40.dp))

        Button(
            onClick = onBackToLogin,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isHighContrast) Color.Yellow else Color(0xFF2196F3),
                contentColor = if (isHighContrast) Color.Black else Color.White
            )
        ) {
            Icon(Icons.Default.Login, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Go Back to Login Page", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        OutlinedButton(
            onClick = onGoToHome,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            border = androidx.compose.foundation.BorderStroke(1.dp, if (isHighContrast) Color.White else Color.LightGray)
        ) {
            Icon(Icons.Default.Home, contentDescription = null, tint = if (isDark) Color.White else Color.Gray)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Go to Home", color = if (isDark) Color.White else Color.Gray)
        }
    }
}

@Composable
fun SuccessItem(icon: androidx.compose.ui.graphics.vector.ImageVector, text: String, color: Color, isHighContrast: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (isHighContrast) Color.Black else color.copy(alpha = 0.05f),
        border = if (isHighContrast) androidx.compose.foundation.BorderStroke(1.dp, Color.White) else null
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(icon, contentDescription = null, tint = color, modifier = Modifier.size(20.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = if (isHighContrast) Color.White else color)
        }
    }
}
