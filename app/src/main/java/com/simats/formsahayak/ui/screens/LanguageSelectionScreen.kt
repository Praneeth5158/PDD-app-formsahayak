package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Translate
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.formsahayak.R

data class Language(
    val name: String,
    val nativeName: String,
    val code: String
)

@Composable
fun LanguageSelectionScreen(
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onContinueClick: (Language) -> Unit
) {
    val backgroundColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF121212) else Color(0xFFF0F7FF)
    val cardColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF1E1E1E) else Color.White

    val languages = listOf(
        Language("English", "English", "en"),
        Language("Telugu", "తెలుగు", "te"),
        Language("Tamil", "தமிழ்", "ta"),
        Language("Hindi", "हिन्दी", "hi")
    )

    var selectedLanguage by remember { mutableStateOf(languages[0]) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(60.dp))

            // Top Icon
            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = if (isHighContrast) Color.Yellow else Color(0xFF7C4DFF) // Purple
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.Translate,
                        contentDescription = null,
                        tint = if (isHighContrast) Color.Black else Color.White,
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = stringResource(R.string.select_language),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = if (isHighContrast) Color.Yellow else if (isDarkMode) Color.White else Color(0xFF1A237E),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(32.dp))

            // Language Options Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = cardColor,
                shadowElevation = if (isHighContrast) 0.dp else 4.dp,
                border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    languages.forEach { language ->
                        LanguageOption(
                            language = language,
                            isSelected = selectedLanguage == language,
                            isDarkMode = isDarkMode,
                            isHighContrast = isHighContrast,
                            onClick = { selectedLanguage = language }
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }

        Button(
            onClick = { onContinueClick(selectedLanguage) },
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isHighContrast) Color.Yellow else Color(0xFF2196F3),
                contentColor = if (isHighContrast) Color.Black else Color.White
            )
        ) {
            Text(stringResource(R.string.continue_btn), fontSize = 16.sp, fontWeight = FontWeight.Bold)
        }
        
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun LanguageOption(
    language: Language,
    isSelected: Boolean,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onClick: () -> Unit
) {
    val isDark = isDarkMode || isHighContrast
    val borderColor = if (isSelected) {
        if (isHighContrast) Color.Yellow else Color(0xFF2196F3)
    } else {
        if (isHighContrast) Color.White else Color(0xFFE0E0E0)
    }
    
    val bgColor = if (isSelected) {
        if (isHighContrast) Color(0xFF333300) else if (isDarkMode) Color(0xFF1A237E).copy(alpha = 0.3f) else Color(0xFFE3F2FD)
    } else {
        Color.Transparent
    }

    val textColor = if (isDark) Color.White else Color.Black
    val selectedTextColor = if (isHighContrast) Color.Yellow else if (isDarkMode) Color.White else Color(0xFF2196F3)

    Box(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = if (isSelected) 2.dp else 1.dp,
                color = borderColor,
                shape = RoundedCornerShape(16.dp)
            )
            .background(
                color = bgColor,
                shape = RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = language.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (isSelected) selectedTextColor else textColor
                )
                Text(
                    text = language.nativeName,
                    fontSize = 14.sp,
                    color = if (isDark) Color.LightGray else Color.Gray
                )
            }
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (isHighContrast) Color.Yellow else Color(0xFF2196F3)
                )
            }
        }
    }
}
