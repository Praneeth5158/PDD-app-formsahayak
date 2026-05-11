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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

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

    // Translation logic
    val title = when (selectedLanguage?.code) {
        "te" -> "లోపాలు కనుగొనబడ్డాయి"
        "ta" -> "பிழைகள் கண்டறியப்பட்டன"
        else -> "Errors Found"
    }
    val errorSummary = when (selectedLanguage?.code) {
        "te" -> "3 లోపాలు కనుగొనబడ్డాయి"
        "ta" -> "3 பிழைகள் கண்டறியப்பட்டன"
        else -> "3 Errors Found"
    }
    val checkFixText = when (selectedLanguage?.code) {
        "te" -> "దయచేసి వాటిని తనిఖీ చేసి సరిదిద్దండి"
        "ta" -> "அவற்றைச் சரிபார்த்து சரிசெய்யவும்"
        else -> "Please check and fix them"
    }
    val fixErrorsButton = when (selectedLanguage?.code) {
        "te" -> "లోపాలను సరిదిద్దండి"
        "ta" -> "பிழைகளைச் சரிசெய்யவும்"
        else -> "Fix Errors"
    }
    val needHelpText = when (selectedLanguage?.code) {
        "te" -> "సహాయం కావాలా?"
        "ta" -> "உதவி தேவையா?"
        else -> "NEED HELP?"
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        title, 
                        fontWeight = FontWeight.ExtraBold, 
                        color = textColor
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = "Back", 
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
                        Text(text = errorSummary, color = Color.White, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                        Text(text = checkFixText, color = Color.White.copy(alpha = 0.9f), fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            ErrorItem(
                title = if (selectedLanguage?.code == "te") "సంతకం" else if (selectedLanguage?.code == "ta") "கையெழுத்து" else "Signature",
                description = if (selectedLanguage?.code == "te") "సంతకం లేదు. దయచేసి నిర్దేశించిన పెట్టెలో సంతకం చేయండి." else if (selectedLanguage?.code == "ta") "கையெழுத்து இல்லை. ஒதுக்கப்பட்ட பெட்டியில் கையெழுத்திடவும்." else "Signature missing. Please sign in the designated box.",
                iconColor = Color(0xFFFF5252),
                textColor = textColor,
                cardColor = cardColor,
                isHighContrast = isHighContrast
            )
            
            Spacer(modifier = Modifier.height(16.dp))

            ErrorItem(
                title = if (selectedLanguage?.code == "te") "పేరు" else if (selectedLanguage?.code == "ta") "பெயர்" else "Name",
                description = if (selectedLanguage?.code == "te") "పేరు స్పష్టంగా లేదు. మీ పూర్తి పేరును స్పష్టంగా CAPITAL అక్షరాలలో రాయండి." else if (selectedLanguage?.code == "ta") "பெயர் தெளிவாக இல்லை. உங்கள் முழுப் பெயரையும் பெரிய எழுத்துக்களில் (CAPITAL letters) தெளிவாக எழுதவும்." else "Name not clear. Write your full name clearly in CAPITAL letters.",
                iconColor = Color(0xFFFF9800),
                textColor = textColor,
                cardColor = cardColor,
                isHighContrast = isHighContrast
            )

            Spacer(modifier = Modifier.height(16.dp))

            ErrorItem(
                title = if (selectedLanguage?.code == "te") "తేదీ" else if (selectedLanguage?.code == "ta") "தேதி" else "Date",
                description = if (selectedLanguage?.code == "te") "చెల్లని తేదీ ఫార్మాట్. పేర్కొన్న విధంగా DD/MM/YYYY ఫార్మాట్‌ను ఉపయోగించండి." else if (selectedLanguage?.code == "ta") "தவறான தேதி வடிவம். குறிப்பிட்டபடி DD/MM/YYYY வடிவத்தைப் பயன்படுத்தவும்." else "Invalid date format. Use DD/MM/YYYY format as specified.",
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
                Text(fixErrorsButton, fontSize = 16.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            TextButton(onClick = { }) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Info, contentDescription = null, modifier = Modifier.size(18.dp), tint = if (isDark) Color.LightGray else Color.Gray)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(needHelpText, color = if (isDark) Color.LightGray else Color.Gray, fontWeight = FontWeight.ExtraBold)
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
