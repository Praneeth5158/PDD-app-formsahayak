package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.SlowMotionVideo
import androidx.compose.material.icons.filled.Speed
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class VoiceSpeedStrings(
    val title: String,
    val subtitle: String,
    val slow: String,
    val slowDesc: String,
    val normal: String,
    val normalDesc: String,
    val fast: String,
    val fastDesc: String,
    val footer: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceSpeedSelectionScreen(
    currentSpeed: String,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    selectedLanguage: Language?,
    onSpeedSelected: (String) -> Unit,
    onBackClick: () -> Unit
) {
    val labels = when (selectedLanguage?.code) {
        "te" -> VoiceSpeedStrings(
            title = "వాయిస్ వేగం",
            subtitle = "వాయిస్ గైడెన్స్ ఎంత వేగంగా మాట్లాడాలో ఎంచుకోండి",
            slow = "నెమ్మదిగా",
            slowDesc = "మెరుగైన అవగాహన కోసం నెమ్మదిగా మాట్లాడటం",
            normal = "సాధారణం",
            normalDesc = "సాధారణ మాట్లాడే వేగం",
            fast = "వేగంగా",
            fastDesc = "త్వరిత నావిగేషన్ కోసం వేగంగా మాట్లాడటం",
            footer = "మీరు సెట్టింగ్‌ల నుండి ఎప్పుడైనా ఈ సెట్టింగ్‌ను మార్చవచ్చు"
        )
        "ta" -> VoiceSpeedStrings(
            title = "குரல் வேகம்",
            subtitle = "குரல் வழிகாட்டுதல் எவ்வளவு வேகமாகப் பேச வேண்டும் என்பதைத் தேர்வுசெய்யவும்",
            slow = "மெதுவாக",
            slowDesc = "சிறந்த புரிதலுக்காக மெதுவான பேச்சு",
            normal = "சாதாரணமானது",
            normalDesc = "சாதாரண பேசும் வேகம்",
            fast = "வேகமாக",
            fastDesc = "விரைவான வழிசெலுத்தலுக்கு வேகமான பேச்சு",
            footer = "அமைப்புகளிலிருந்து எந்த நேரத்திலும் இந்த அமைப்பை மாற்றலாம்"
        )
        else -> VoiceSpeedStrings(
            title = "Voice Speed",
            subtitle = "Choose how fast the voice guidance should speak",
            slow = "Slow",
            slowDesc = "Slower speech for better understanding",
            normal = "Normal",
            normalDesc = "Standard speaking speed",
            fast = "Fast",
            fastDesc = "Faster speech for quick navigation",
            footer = "You can change this setting anytime from Settings"
        )
    }

    val backgroundColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF121212) else Color(0xFFF8FBFF)
    val textColor = if (isDarkMode || isHighContrast) Color.White else Color(0xFF2C3E50)
    val secondaryTextColor = if (isDarkMode || isHighContrast) Color.LightGray else Color.Gray

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(labels.title, fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor,
                    titleContentColor = textColor,
                    navigationIconContentColor = textColor
                )
            )
        },
        containerColor = backgroundColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = labels.subtitle,
                fontSize = 14.sp,
                color = secondaryTextColor,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            SpeedOptionItem(
                title = labels.slow,
                description = labels.slowDesc,
                icon = Icons.Default.SlowMotionVideo,
                iconBgColor = Color(0xFFFFF3E0),
                iconColor = Color(0xFFF57C00),
                isSelected = currentSpeed == "Slow",
                isDarkMode = isDarkMode,
                isHighContrast = isHighContrast,
                onClick = { onSpeedSelected("Slow") }
            )

            SpeedOptionItem(
                title = labels.normal,
                description = labels.normalDesc,
                icon = Icons.Default.Speed,
                iconBgColor = Color(0xFFE3F2FD),
                iconColor = Color(0xFF1976D2),
                isSelected = currentSpeed == "Normal",
                isDarkMode = isDarkMode,
                isHighContrast = isHighContrast,
                onClick = { onSpeedSelected("Normal") }
            )

            SpeedOptionItem(
                title = labels.fast,
                description = labels.fastDesc,
                icon = Icons.Default.Bolt,
                iconBgColor = Color(0xFFE8F5E9),
                iconColor = Color(0xFF2E7D32),
                isSelected = currentSpeed == "Fast",
                isDarkMode = isDarkMode,
                isHighContrast = isHighContrast,
                onClick = { onSpeedSelected("Fast") }
            )

            Spacer(modifier = Modifier.height(8.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = if (isHighContrast) Color.Black else Color(0xFFE8F5E9).copy(alpha = 0.5f),
                border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
            ) {
                Text(
                    text = labels.footer,
                    fontSize = 12.sp,
                    color = if (isHighContrast) Color.White else Color(0xFF2E7D32),
                    modifier = Modifier.padding(12.dp),
                    fontWeight = FontWeight.Medium
                )
            }
        }
    }
}

@Composable
private fun SpeedOptionItem(
    title: String,
    description: String,
    icon: ImageVector,
    iconBgColor: Color,
    iconColor: Color,
    isSelected: Boolean,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onClick: () -> Unit
) {
    val cardBg = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkMode || isHighContrast) Color.White else Color.Black
    val borderColor = if (isSelected) {
        if (isHighContrast) Color.White else Color(0xFF2196F3)
    } else {
        if (isHighContrast) Color.Gray else Color.Transparent
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = if (isSelected && !isHighContrast && !isDarkMode) Color(0xFFE3F2FD).copy(alpha = 0.5f) else cardBg,
        border = BorderStroke(if (isSelected || isHighContrast) 2.dp else 0.dp, borderColor),
        shadowElevation = if (isHighContrast) 0.dp else 2.dp
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = if (isHighContrast) Color.White else iconBgColor
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isHighContrast) Color.Black else iconColor
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp,
                    color = textColor
                )
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = if (isDarkMode || isHighContrast) Color.LightGray else Color.Gray
                )
            }
            
            if (isSelected) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Selected",
                    tint = if (isHighContrast) Color.White else Color(0xFF2196F3)
                )
            } else {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                    contentDescription = null,
                    tint = if (isDarkMode || isHighContrast) Color.White else Color.LightGray
                )
            }
        }
    }
}
