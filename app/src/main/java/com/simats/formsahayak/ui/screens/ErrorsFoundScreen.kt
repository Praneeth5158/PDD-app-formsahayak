package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.formsahayak.R

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ErrorsFoundScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onBackClick: () -> Unit,
    onFixErrorsClick: () -> Unit
) {
    val isDark = isDarkMode || isHighContrast
    val backgroundColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF121212) else Color(0xFFF8FBFF)
    val cardColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.errors_found), 
                        fontWeight = FontWeight.ExtraBold, 
                        color = textColor
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = stringResource(R.string.back), 
                            tint = textColor
                        )
                    }
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = backgroundColor
                )
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            val errorGradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFFFF5252), Color(0xFFFF8A65))
            )
            
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = Color.Transparent,
                border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
            ) {
                val rowBackgroundModifier = if (isHighContrast) {
                    Modifier.background(Color.Black)
                } else {
                    Modifier.background(errorGradient)
                }
                
                Row(
                    modifier = rowBackgroundModifier.padding(20.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Error, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(text = stringResource(R.string.errors_count, 3), color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = stringResource(R.string.check_fix_errors), color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            ErrorItem(
                title = stringResource(R.string.signature),
                description = stringResource(R.string.signature_error),
                iconColor = Color(0xFFFF5252),
                textColor = textColor,
                cardColor = cardColor,
                isHighContrast = isHighContrast
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            ErrorItem(
                title = stringResource(R.string.full_name),
                description = stringResource(R.string.name_error),
                iconColor = Color(0xFFFF9800),
                textColor = textColor,
                cardColor = cardColor,
                isHighContrast = isHighContrast
            )

            Spacer(modifier = Modifier.height(16.dp))

            ErrorItem(
                title = stringResource(R.string.date),
                description = stringResource(R.string.date_error),
                iconColor = Color(0xFFFF5252),
                textColor = textColor,
                cardColor = cardColor,
                isHighContrast = isHighContrast
            )

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = onFixErrorsClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isHighContrast) Color.Yellow else Color(0xFF2196F3),
                    contentColor = if (isHighContrast) Color.Black else Color.White
                )
            ) {
                Text(stringResource(R.string.fix_errors), fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(18.dp), tint = if (isDark) Color.LightGray else Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(stringResource(R.string.need_help), color = if (isDark) Color.LightGray else Color.Gray, fontWeight = FontWeight.ExtraBold)
                }
            }
        }
    }
}

@Composable
fun ErrorItem(title: String, description: String, iconColor: Color, textColor: Color, cardColor: Color, isHighContrast: Boolean) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = cardColor,
        shadowElevation = if (isHighContrast) 0.dp else 2.dp,
        border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(modifier = Modifier.size(12.dp), shape = CircleShape, color = iconColor) {}
                Spacer(modifier = Modifier.width(12.dp))
                Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textColor)
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = description, color = if (textColor == Color.White) Color.LightGray else Color.Gray, fontSize = 14.sp, lineHeight = 20.sp)
        }
    }
}
