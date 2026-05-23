package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Description
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.formsahayak.R

@Composable
fun WelcomeScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean = false,
    isHighContrast: Boolean = false,
    onGetStarted: () -> Unit
) {
    val gradient = if (isHighContrast) {
        Brush.linearGradient(listOf(Color.Black, Color.Black))
    } else {
        Brush.linearGradient(
            colors = listOf(
                Color(0xFF2196F3),
                Color(0xFF00BCD4),
                Color(0xFF4CAF50)
            ),
            start = Offset(0f, 0f),
            end = Offset(1000f, 2000f)
        )
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(gradient)
            .clickable { onGetStarted() }
    ) {
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.2f),
            modifier = Modifier
                .padding(top = 80.dp, start = 40.dp)
                .size(24.dp)
        )
        Icon(
            imageVector = Icons.Default.AutoAwesome,
            contentDescription = null,
            tint = Color.White.copy(alpha = 0.2f),
            modifier = Modifier
                .align(Alignment.BottomStart)
                .padding(bottom = 160.dp, start = 60.dp)
                .size(20.dp)
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(contentAlignment = Alignment.TopEnd) {
                Surface(
                    modifier = Modifier
                        .size(240.dp, 140.dp)
                        .padding(top = 8.dp, end = 8.dp),
                    shape = RoundedCornerShape(20.dp),
                    color = if (isHighContrast) Color.Black else Color.White,
                    shadowElevation = if (isHighContrast) 0.dp else 8.dp,
                    border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
                ) {
                    Box(contentAlignment = Alignment.CenterStart, modifier = Modifier.padding(start = 24.dp)) {
                        Icon(
                            imageVector = Icons.Default.Description,
                            contentDescription = null,
                            tint = if (isHighContrast) Color.Yellow else Color(0xFF2196F3),
                            modifier = Modifier.size(60.dp)
                        )
                    }
                }
                
                Surface(
                    modifier = Modifier.size(32.dp),
                    shape = CircleShape,
                    color = if (isHighContrast) Color.Yellow else Color(0xFFFFD600),
                    shadowElevation = if (isHighContrast) 0.dp else 4.dp,
                    border = if (isHighContrast) androidx.compose.foundation.BorderStroke(1.dp, Color.Black) else null
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = if (isHighContrast) Color.Black else Color.White,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            Text(
                text = stringResource(R.string.app_name),
                fontSize = 28.sp, 
                fontWeight = FontWeight.ExtraBold,
                color = if (isHighContrast) Color.Yellow else Color.White,
                textAlign = TextAlign.Center,
                letterSpacing = (-1).sp
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.smart_guidance),
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Text(
                text = stringResource(R.string.tap_to_get_started),
                color = if (isHighContrast) Color.Yellow else Color.White,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(16.dp))

            Row(verticalAlignment = Alignment.CenterVertically) {
                Dot(isSelected = false, isHighContrast = isHighContrast)
                Dot(isSelected = true, isHighContrast = isHighContrast)
                Dot(isSelected = false, isHighContrast = isHighContrast)
            }
        }
    }
}

@Composable
private fun Dot(isSelected: Boolean, isHighContrast: Boolean) {
    val color = if (isSelected) {
        if (isHighContrast) Color.Yellow else Color.White
    } else {
        if (isHighContrast) Color.Gray else Color.White.copy(alpha = 0.5f)
    }
    val size = if (isSelected) 10.dp else 6.dp
    Box(
        modifier = Modifier
            .padding(horizontal = 4.dp)
            .size(size)
            .background(color, CircleShape)
    )
}

