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
fun VerificationFailedScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onTryAgain: () -> Unit,
    onBackToLogin: () -> Unit,
    onGoBack: () -> Unit
) {
    val isDark = isDarkMode || isHighContrast
    val backgroundColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF121212) else Color(0xFFFFF8F8)
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

        // Failed Icon
        Box(contentAlignment = Alignment.Center) {
            Surface(
                modifier = Modifier.size(100.dp),
                shape = CircleShape,
                color = if (isDark) Color(0xFF310000) else Color(0xFFFFEBEE)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Cancel,
                        contentDescription = null,
                        tint = if (isHighContrast) Color.Yellow else Color(0xFFF44336),
                        modifier = Modifier.size(60.dp)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Verification Failed",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            textAlign = TextAlign.Center,
            color = if (isHighContrast) Color.Yellow else textColor
        )

        Text(
            text = "OTP could not be verified",
            fontSize = 14.sp,
            color = secondaryTextColor,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(32.dp))

        // Error Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF2D1212) else Color(0xFFFFEBEE),
            border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFFFCDD2))
        ) {
            Row(modifier = Modifier.padding(16.dp)) {
                Icon(Icons.Default.ErrorOutline, contentDescription = null, tint = if (isHighContrast) Color.Yellow else Color(0xFFD32F2F))
                Spacer(modifier = Modifier.width(12.dp))
                Column {
                    Text("Unable to Verify OTP", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = if (isHighContrast) Color.White else Color(0xFFC62828))
                    Text(
                        "The verification code you entered is incorrect or has expired. Please try again.",
                        fontSize = 12.sp,
                        color = if (isHighContrast) Color.White else Color(0xFFD32F2F)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        Text(
            "Common Reasons:",
            modifier = Modifier.fillMaxWidth(),
            textAlign = TextAlign.Start,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            color = textColor
        )
        
        Spacer(modifier = Modifier.height(16.dp))

        ReasonItem(icon = Icons.Default.Timer, title = "OTP Expired", subtitle = "The OTP might have expired (valid for 10 minutes)", isDark = isDark, isHighContrast = isHighContrast)
        Spacer(modifier = Modifier.height(12.dp))
        ReasonItem(icon = Icons.Default.Dialpad, title = "Incorrect Code", subtitle = "Double-check the 6-digit code you entered", isDark = isDark, isHighContrast = isHighContrast)
        Spacer(modifier = Modifier.height(12.dp))
        ReasonItem(icon = Icons.Default.SignalCellularConnectedNoInternet0Bar, title = "Network Issue", subtitle = "Poor connection may have caused verification fail", isDark = isDark, isHighContrast = isHighContrast)

        Spacer(modifier = Modifier.height(32.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(8.dp),
            color = if (isDark) Color(0xFF121212) else Color(0xFFE3F2FD),
            border = if (isHighContrast) androidx.compose.foundation.BorderStroke(1.dp, Color.White) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFBBDEFB))
        ) {
            Column(modifier = Modifier.padding(12.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                Text("Need Help?", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = if (isHighContrast) Color.Yellow else Color(0xFF1976D2))
                Text("Contact support or try requesting a new OTP", fontSize = 12.sp, color = if (isHighContrast) Color.White else Color(0xFF2196F3))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onTryAgain,
            modifier = Modifier.fillMaxWidth().height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isHighContrast) Color.Yellow else Color(0xFFFF5722),
                contentColor = if (isHighContrast) Color.Black else Color.White
            )
        ) {
            Icon(Icons.Default.Refresh, contentDescription = null)
            Spacer(modifier = Modifier.width(8.dp))
            Text("Try Again with New OTP", fontWeight = FontWeight.Bold)
        }

        Spacer(modifier = Modifier.height(12.dp))

        TextButton(onClick = onBackToLogin) {
            Icon(Icons.Default.ArrowBack, contentDescription = null, tint = if (isDark) Color.White else Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Go Back to Login Page", color = if (isDark) Color.White else Color.Gray)
        }

        TextButton(onClick = onGoBack) {
            Icon(Icons.Default.ChevronLeft, contentDescription = null, tint = if (isDark) Color.White else Color.Gray, modifier = Modifier.size(16.dp))
            Spacer(modifier = Modifier.width(8.dp))
            Text("Go Back", color = if (isDark) Color.White else Color.Gray)
        }
    }
}

@Composable
fun ReasonItem(icon: androidx.compose.ui.graphics.vector.ImageVector, title: String, subtitle: String, isDark: Boolean, isHighContrast: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        color = if (isHighContrast) Color.Black else if (isDark) Color(0xFF1E1E1E) else Color.White,
        shadowElevation = if (isHighContrast) 0.dp else 1.dp,
        border = if (isHighContrast) androidx.compose.foundation.BorderStroke(1.dp, Color.White) else null
    ) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Surface(modifier = Modifier.size(36.dp), shape = RoundedCornerShape(8.dp), color = if (isDark) Color(0xFF333333) else Color(0xFFF5F5F5)) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(icon, contentDescription = null, tint = if (isHighContrast) Color.Yellow else Color.Gray, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isDark) Color.White else Color.Black)
                Text(subtitle, fontSize = 11.sp, color = if (isDark) Color.LightGray else Color.Gray)
            }
        }
    }
}
