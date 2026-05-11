package com.simats.formsahayak.ui.screens

import androidx.activity.compose.BackHandler
import androidx.annotation.OptIn
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material.icons.filled.FullscreenExit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView
import com.simats.formsahayak.R

private data class VideoTutorialStrings(
    val title: String,
    val videoStatus: String,
    val aboutTitle: String,
    val aboutDesc: String,
    val points: List<String>
)

@OptIn(UnstableApi::class)
@Composable
fun VideoTutorialScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean = false,
    isHighContrast: Boolean = false,
    onCloseClick: () -> Unit
) {
    val context = LocalContext.current
    var isFullScreen by remember { mutableStateOf(false) }

    if (isFullScreen) {
        BackHandler { isFullScreen = false }
    }

    val labels = when (selectedLanguage?.code) {
        "te" -> VideoTutorialStrings(
            title = "ఫారమ్ సహాయక్ ఎలా ఉపయోగించాలి",
            videoStatus = "వీడియో ప్లే అవుతోంది",
            aboutTitle = "ఈ ట్యుటోరియల్ గురించి",
            aboutDesc = "ఈ సమగ్ర వీడియో గైడ్ మీకు ఫారమ్ సహాయక్ యొక్క అన్ని ఫీచర్ల ద్వారా నడిపిస్తుంది, మీకు ఎలా చేయాలో చూపుతుంది:",
            points = listOf(
                "బ్యాంక్ ఫారమ్‌లను అప్‌లోడ్ చేయండి మరియు స్కాన్ చేయండి",
                "మీకు నచ్చిన భాషలో వాయిస్ గైడెన్స్‌ని ఉపయోగించండి",
                "ఫారమ్ ఫీల్డ్‌ల ద్వారా సులభంగా నావిగేట్ చేయండి",
                "ఫారమ్‌లను సరిగ్గా పూర్తి చేయండి మరియు సమర్పించండి"
            )
        )
        "ta" -> VideoTutorialStrings(
            title = "FormSahayak ఐ எவ்வாறு பயன்படுத்துவது",
            videoStatus = "வீடியோ இயங்குகிறது",
            aboutTitle = "இந்த டுடோரியல் பற்றி",
            aboutDesc = "இந்த விரிவான வீடியோ வழிகாட்டி FormSahayak இன் அனைத்து அம்சங்களையும் உங்களுக்கு வழங்கும், எப்படி செய்வது என்பதைக் காட்டும்:",
            points = listOf(
                "வங்கி படிவங்களைப் பதிவேற்றவும் மற்றும் ஸேன் செய்யவும்",
                "உங்களுக்கு விருப்பமான மொழியில் குரல் வழிகாட்டலைப் பயன்படுத்தவும்",
                "படிவப் புலங்கள் வழியாக எளிதாக செல்லவும்",
                "படிவங்களைச் சரியாகப் பூர்த்தி செய்து சமர்ப்பிக்கவும்"
            )
        )
        else -> VideoTutorialStrings(
            title = "How to Use FormSahayak",
            videoStatus = "Video Playing",
            aboutTitle = "About This Tutorial",
            aboutDesc = "This comprehensive video guide will walk you through all the features of FormSahayak, showing you how to:",
            points = listOf(
                "Upload and scan bank forms",
                "Use voice guidance in your preferred language",
                "Navigate through form fields easily",
                "Complete and submit forms correctly"
            )
        )
    }

    val backgroundColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF121212) else Color(0xFFF8FBFF)
    val cardColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkMode || isHighContrast) Color.White else Color(0xFF2C3E50)
    val secondaryTextColor = if (isDarkMode || isHighContrast) Color.LightGray else Color.Gray
    val accentColor = if (isHighContrast) Color.Yellow else Color(0xFF2196F3)

    // ExoPlayer Setup
    val exoPlayer = remember {
        ExoPlayer.Builder(context).build().apply {
            val videoUri = "android.resource://${context.packageName}/${R.raw.tutorial}".toUri()
            setMediaItem(MediaItem.fromUri(videoUri))
            prepare()
            playWhenReady = true
        }
    }

    DisposableEffect(Unit) {
        onDispose {
            exoPlayer.release()
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(if (isFullScreen) 0.dp else 16.dp)
    ) {
        if (!isFullScreen) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = onCloseClick,
                    modifier = Modifier
                        .background(if (isDarkMode || isHighContrast) Color.White.copy(alpha = 0.1f) else Color.Black.copy(alpha = 0.05f), CircleShape)
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = textColor)
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = labels.title,
                    color = textColor,
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }

        // Real Video Player
        Surface(
            modifier = if (isFullScreen) {
                Modifier.fillMaxSize()
            } else {
                Modifier
                    .fillMaxWidth()
                    .height(220.dp)
                    .then(if (isHighContrast) Modifier.border(2.dp, Color.White, RoundedCornerShape(12.dp)) else Modifier)
            },
            shape = if (isFullScreen) RoundedCornerShape(0.dp) else RoundedCornerShape(12.dp),
            color = Color.Black
        ) {
            Box {
                AndroidView(
                    factory = { ctx ->
                        PlayerView(ctx).apply {
                            player = exoPlayer
                            useController = true
                            setShowNextButton(false)
                            setShowPreviousButton(false)
                        }
                    },
                    modifier = Modifier.fillMaxSize()
                )

                IconButton(
                    onClick = { isFullScreen = !isFullScreen },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(8.dp)
                        .background(Color.Black.copy(alpha = 0.5f), CircleShape)
                ) {
                    Icon(
                        imageVector = if (isFullScreen) Icons.Default.FullscreenExit else Icons.Default.Fullscreen,
                        contentDescription = "Toggle Fullscreen",
                        tint = Color.White
                    )
                }
            }
        }

        if (!isFullScreen) {
            Spacer(modifier = Modifier.height(32.dp))

            // About Tutorial Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = cardColor,
                border = if (isHighContrast) androidx.compose.foundation.BorderStroke(1.dp, Color.White) else null,
                shadowElevation = if (isHighContrast) 0.dp else 2.dp
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = labels.aboutTitle,
                        color = textColor,
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = labels.aboutDesc,
                        color = secondaryTextColor,
                        fontSize = 14.sp,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    labels.points.forEach { point ->
                        Row(
                            modifier = Modifier.padding(vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(6.dp)
                                    .background(accentColor, CircleShape)
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = point,
                                color = textColor.copy(alpha = 0.9f),
                                fontSize = 14.sp
                            )
                        }
                    }
                }
            }
        }
    }
}
