package com.simats.formsahayak.ui.screens

import android.graphics.Bitmap
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.formsahayak.ui.viewmodel.FormViewModel
import kotlinx.coroutines.delay

@Composable
fun ScanningProgressScreen(
    imageBitmap: Bitmap?,
    selectedLanguage: Language?,
    viewModel: FormViewModel,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    statusText: String = "Scanning...",
    subText: String = "Detecting document edges...",
    onScanningComplete: () -> Unit
) {
    var progress by remember { mutableFloatStateOf(0f) }
    
    LaunchedEffect(statusText) {
        progress = 0f
        val duration = 2000L
        val steps = 100
        val stepDelay = duration / steps
        for (i in 1..steps) {
            delay(stepDelay)
            progress = i.toFloat() / steps
        }
        onScanningComplete()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(if (isHighContrast) Color.Black else Color(0xFF2563EB)),
        contentAlignment = Alignment.Center
    ) {
        Surface(
            modifier = Modifier
                .width(300.dp)
                .wrapContentHeight(),
            shape = RoundedCornerShape(24.dp),
            color = if (isHighContrast) Color.Black else Color.White,
            shadowElevation = if (isHighContrast) 0.dp else 12.dp,
            border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.Yellow) else null
        ) {
            Column(
                modifier = Modifier.padding(32.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Surface(
                    modifier = Modifier.size(80.dp),
                    shape = CircleShape,
                    color = if (isHighContrast) Color.Yellow else Color(0xFFEFF6FF)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = if (isHighContrast) Color.Black else Color(0xFF2563EB),
                            modifier = Modifier.size(36.dp)
                        )
                    }
                }
                
                Spacer(modifier = Modifier.height(28.dp))
                
                Text(
                    statusText,
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isHighContrast) Color.Yellow else Color.Black
                )
                
                Spacer(modifier = Modifier.height(8.dp))
                
                Text(
                    subText,
                    fontSize = 14.sp,
                    color = if (isHighContrast) Color.White else Color.Gray,
                    textAlign = TextAlign.Center,
                    lineHeight = 20.sp
                )
                
                Spacer(modifier = Modifier.height(40.dp))
                
                // Progress Bar Container
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(if (isHighContrast) Color.DarkGray else Color(0xFFF1F5F9))
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth(progress)
                            .fillMaxHeight()
                            .background(if (isHighContrast) Color.Yellow else Color.Black)
                    )
                }
                
                Spacer(modifier = Modifier.height(16.dp))
                
                Text(
                    "${(progress * 100).toInt()}%",
                    fontSize = 14.sp,
                    color = if (isHighContrast) Color.Yellow else Color.Gray,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
