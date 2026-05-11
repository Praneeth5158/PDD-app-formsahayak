package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Lightbulb
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.launch

data class OnboardingPage(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val color: Color,
    val bgColor: Color
)

@Composable
fun OnboardingScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onFinished: () -> Unit
) {
    val isDark = isDarkMode || isHighContrast
    val backgroundColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF121212) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryTextColor = if (isDark) Color.LightGray else Color.Gray

    val onboardingPages = listOf(
        OnboardingPage(
            title = when (selectedLanguage?.code) {
                "te" -> "బ్యాంక్ ఫారమ్‌లను స్కాన్ చేయండి"
                "ta" -> "வங்கி படிவங்களை ஸ்கேன் செய்யுங்கள்"
                else -> "Scan Bank Forms"
            },
            description = when (selectedLanguage?.code) {
                "te" -> "బ్యాంక్ ఫారమ్‌లను సులభంగా స్కాన్ చేయడానికి మీ ఫోన్ కెమెరాను ఉపయోగించండి"
                "ta" -> "வங்கி படிவங்களை எளிதாக ஸ்கேன் செய்ய உங்கள் தொலைபேசி கேமராவைப் பயன்படுத்தவும்"
                else -> "Use your phone camera to scan bank forms easily"
            },
            icon = Icons.Default.CameraAlt,
            color = if (isHighContrast) Color.Yellow else Color(0xFF2196F3),
            bgColor = if (isHighContrast) Color.DarkGray else Color(0xFFE3F2FD)
        ),
        OnboardingPage(
            title = when (selectedLanguage?.code) {
                "te" -> "ముఖ్యాంశాలు"
                "ta" -> "சிறப்பம்சங்கள்"
                else -> "Highlighted Fields"
            },
            description = when (selectedLanguage?.code) {
                "te" -> "మీరు నింపాల్సిన ముఖ్యమైన ఫీల్డ్‌లను యాప్ హైలైట్ చేస్తుంది"
                "ta" -> "நீங்கள் நிரப்ப வேண்டிய முக்கியமான புலங்களை ஆப்ஸ் சிறப்பித்துக் காட்டுகிறது"
                else -> "App highlights important fields you need to fill"
            },
            icon = Icons.Default.Lightbulb,
            color = if (isHighContrast) Color.Yellow else Color(0xFF4CAF50),
            bgColor = if (isHighContrast) Color.DarkGray else Color(0xFFE8F5E9)
        ),
        OnboardingPage(
            title = when (selectedLanguage?.code) {
                "te" -> "వాయిస్ గైడెన్స్"
                "ta" -> "குரல் வழிகாட்டுதல்"
                else -> "Voice Guidance"
            },
            description = when (selectedLanguage?.code) {
                "te" -> "మీ ప్రాంతీయ భాషలో వాయిస్ సూచనలను పొందండి"
                "ta" -> "உங்கள் பிராந்திய மொழியில் குரல் வழிமுறைகளைப் பெறுங்கள்"
                else -> "Get voice instructions in your regional language"
            },
            icon = Icons.Default.VolumeUp,
            color = if (isHighContrast) Color.Yellow else Color(0xFF9C27B0),
            bgColor = if (isHighContrast) Color.DarkGray else Color(0xFFF3E5F5)
        )
    )

    val pagerState = rememberPagerState(pageCount = { onboardingPages.size })
    val scope = rememberCoroutineScope()

    val skipText = when (selectedLanguage?.code) {
        "te" -> "వదిలేయండి"
        "ta" -> "தவிர்"
        else -> "Skip"
    }
    val nextText = when (selectedLanguage?.code) {
        "te" -> "తదుపరి >"
        "ta" -> "அடுத்து >"
        else -> "Next >"
    }
    val getStartedText = when (selectedLanguage?.code) {
        "te" -> "ప్రారంభించండి >"
        "ta" -> "தொடங்குங்கள் >"
        else -> "Get Started >"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        HorizontalPager(
            state = pagerState,
            modifier = Modifier.weight(1f)
        ) { pageIndex ->
            val page = onboardingPages[pageIndex]
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Surface(
                    modifier = Modifier.size(160.dp),
                    shape = CircleShape,
                    color = page.bgColor,
                    border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = page.icon,
                            contentDescription = null,
                            tint = page.color,
                            modifier = Modifier.size(80.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                Text(
                    text = page.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = if (isHighContrast) Color.Yellow else textColor
                )

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = page.description,
                    fontSize = 16.sp,
                    color = secondaryTextColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 32.dp)
                )
            }
        }

        // Page Indicator
        Row(
            modifier = Modifier
                .padding(vertical = 24.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            repeat(onboardingPages.size) { iteration ->
                val color = if (pagerState.currentPage == iteration) {
                    if (isHighContrast) Color.Yellow else Color(0xFF2196F3)
                } else {
                    if (isHighContrast) Color.White else Color.LightGray
                }
                Box(
                    modifier = Modifier
                        .padding(4.dp)
                        .size(if (pagerState.currentPage == iteration) 24.dp else 8.dp, 8.dp)
                        .background(color, RoundedCornerShape(4.dp))
                )
            }
        }

        // Bottom Buttons
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            TextButton(
                onClick = onFinished,
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp)
            ) {
                Text(skipText, color = if (isHighContrast) Color.White else secondaryTextColor)
            }

            Spacer(modifier = Modifier.width(16.dp))

            Button(
                onClick = {
                    if (pagerState.currentPage < onboardingPages.size - 1) {
                        scope.launch {
                            pagerState.animateScrollToPage(pagerState.currentPage + 1)
                        }
                    } else {
                        onFinished()
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isHighContrast) Color.Yellow else Color(0xFF2196F3),
                    contentColor = if (isHighContrast) Color.Black else Color.White
                )
            ) {
                Text(
                    text = if (pagerState.currentPage == onboardingPages.size - 1) getStartedText else nextText,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}
