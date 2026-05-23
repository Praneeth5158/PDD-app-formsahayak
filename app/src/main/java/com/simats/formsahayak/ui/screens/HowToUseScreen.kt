package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.formsahayak.R
import com.simats.formsahayak.ui.components.BottomNavigationBar

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HowToUseScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onGotItClick: () -> Unit,
    onHomeClick: () -> Unit,
    onFormsClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onVideoTutorialClick: () -> Unit,
    onGiveFeedbackClick: () -> Unit
) {
    val backgroundColor = when {
        isHighContrast -> Color.Black
        isDarkMode -> Color(0xFF121212)
        else -> Color(0xFFF8FBFF)
    }
    val actualTextColor = if (isHighContrast || isDarkMode) Color.White else Color(0xFF2C3E50)
    val cardColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val secondaryTextColor = if (isHighContrast) Color.White else if (isDarkMode) Color.LightGray else Color.Gray

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.how_to_use), 
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = actualTextColor
                    ) 
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = backgroundColor)
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentScreen = "help",
                isDarkMode = isDarkMode,
                isHighContrast = isHighContrast,
                selectedLanguage = selectedLanguage,
                onHomeClick = onHomeClick,
                onFormsClick = onFormsClick,
                onHelpClick = { },
                onSettingsClick = onSettingsClick,
                onProfileClick = onProfileClick
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
            // Welcome Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = cardColor,
                shadowElevation = if (isHighContrast) 0.dp else 2.dp,
                border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
            ) {
                Column(
                    modifier = Modifier.padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.htu_welcome),
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = actualTextColor,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.htu_subtitle),
                        fontSize = 14.sp,
                        fontWeight = if (isHighContrast) FontWeight.Bold else FontWeight.Normal,
                        color = secondaryTextColor,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Video Tutorial Card
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onVideoTutorialClick() },
                shape = RoundedCornerShape(16.dp),
                color = if (isHighContrast) Color.Black else Color(0xFFFAF5FF),
                shadowElevation = if (isHighContrast) 0.dp else 1.dp,
                border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.PlayCircle,
                            contentDescription = null,
                            tint = Color(0xFF9C27B0),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = stringResource(R.string.htu_video_title),
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = actualTextColor
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.htu_video_desc),
                        fontSize = 12.sp,
                        color = secondaryTextColor
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    // Video Placeholder
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(160.dp)
                            .background(Color(0xFF1A1A1A), RoundedCornerShape(12.dp)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.PlayArrow,
                            contentDescription = "Play Video",
                            tint = Color.White,
                            modifier = Modifier.size(48.dp)
                        )
                    }
                    
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.htu_video_tap),
                        fontSize = 11.sp,
                        color = secondaryTextColor,
                        modifier = Modifier.align(Alignment.CenterHorizontally),
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Steps
            HowToUseStepItem(
                number = 1,
                title = stringResource(R.string.htu_step1_title),
                description = stringResource(R.string.htu_step1_desc),
                icon = Icons.Default.PhotoCamera,
                iconColor = if (isHighContrast) Color.White else Color(0xFF2196F3),
                bgColor = if (isHighContrast) Color.Black else Color(0xFFE3F2FD),
                textColor = actualTextColor,
                isDark = isDarkMode,
                isHighContrast = isHighContrast
            )

            HowToUseStepItem(
                number = 2,
                title = stringResource(R.string.htu_step2_title),
                description = stringResource(R.string.htu_step2_desc),
                icon = Icons.Default.Lightbulb,
                iconColor = if (isHighContrast) Color.White else Color(0xFF4CAF50),
                bgColor = if (isHighContrast) Color.Black else Color(0xFFE8F5E9),
                textColor = actualTextColor,
                isDark = isDarkMode,
                isHighContrast = isHighContrast,
                showInfoIcon = true
            )

            HowToUseStepItem(
                number = 3,
                title = stringResource(R.string.htu_step3_title),
                description = stringResource(R.string.htu_step3_desc),
                icon = Icons.AutoMirrored.Filled.VolumeUp,
                iconColor = if (isHighContrast) Color.White else Color(0xFF9C27B0),
                bgColor = if (isHighContrast) Color.Black else Color(0xFFF3E5F5),
                textColor = actualTextColor,
                isDark = isDarkMode,
                isHighContrast = isHighContrast
            )

            HowToUseStepItem(
                number = 4,
                title = stringResource(R.string.htu_step4_title),
                description = stringResource(R.string.htu_step4_desc),
                icon = Icons.Default.CheckCircle,
                iconColor = if (isHighContrast) Color.White else Color(0xFFFF9800),
                bgColor = if (isHighContrast) Color.Black else Color(0xFFFFF3E0),
                textColor = actualTextColor,
                isDark = isDarkMode,
                isHighContrast = isHighContrast
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Need More Help Banner
            val helpGradient = Brush.horizontalGradient(
                colors = listOf(Color(0xFF2196F3), Color(0xFF00E676))
            )
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = if (isHighContrast) Color.Black else Color.Transparent,
                border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
            ) {
                Box(
                    modifier = (if (isHighContrast) Modifier.background(Color.Black) else Modifier.background(helpGradient)).padding(20.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = stringResource(R.string.htu_need_help),
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.htu_contact_support),
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 12.sp,
                            fontWeight = if (isHighContrast) FontWeight.Bold else FontWeight.Normal,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.Call, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "1800-123-4567",
                                color = Color.White,
                                fontSize = 20.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Share Your Experience Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = cardColor,
                shadowElevation = if (isHighContrast) 0.dp else 1.dp,
                border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
            ) {
                Column(
                    modifier = Modifier.padding(16.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = stringResource(R.string.htu_share_exp),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = actualTextColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = stringResource(R.string.htu_share_desc),
                        fontSize = 12.sp,
                        color = secondaryTextColor,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val feedbackGradient = Brush.horizontalGradient(
                        colors = listOf(Color(0xFF9C27B0), Color(0xFFE91E63))
                    )
                    Button(
                        onClick = onGiveFeedbackClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(48.dp),
                        shape = RoundedCornerShape(12.dp),
                        contentPadding = PaddingValues(0.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
                    ) {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .background(feedbackGradient)
                                .padding(horizontal = 16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Outlined.RateReview, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(stringResource(R.string.htu_give_feedback), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Got It Button
            Button(
                onClick = onGotItClick,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .then(if (isHighContrast) Modifier.border(BorderStroke(2.dp, Color.White), RoundedCornerShape(12.dp)) else Modifier),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(containerColor = if (isHighContrast) Color.Black else Color(0xFF2196F3))
            ) {
                Text(stringResource(R.string.htu_got_it), fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun HowToUseStepItem(
    number: Int,
    title: String,
    description: String,
    icon: ImageVector,
    iconColor: Color,
    bgColor: Color,
    textColor: Color,
    isDark: Boolean,
    isHighContrast: Boolean,
    showInfoIcon: Boolean = false
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp),
        shape = RoundedCornerShape(16.dp),
        color = if (isHighContrast) Color.Black else if (isDark) Color(0xFF1E1E1E) else Color.White,
        shadowElevation = if (isHighContrast) 0.dp else 1.dp,
        border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.Top
        ) {
            Surface(
                modifier = Modifier.size(48.dp),
                shape = CircleShape,
                color = if (isHighContrast) Color.Black else if (isDark) Color(0xFF333333) else bgColor,
                border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = if (isHighContrast) Color.White else iconColor,
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            
            Spacer(modifier = Modifier.width(16.dp))
            
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Surface(
                        modifier = Modifier.size(20.dp),
                        shape = CircleShape,
                        color = if (isHighContrast || isDark) Color.White else Color.Black
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "$number",
                                color = if (isHighContrast || isDark) Color.Black else Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.ExtraBold
                            )
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    if (showInfoIcon) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = if (isHighContrast || isDark) Color.White else Color.Black,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                    }
                    Text(
                        text = title,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textColor
                    )
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = description,
                    fontSize = 12.sp,
                    color = if (isHighContrast) Color.White else if (isDark) Color.LightGray else Color(0xFF757575),
                    lineHeight = 18.sp,
                    fontWeight = if (isHighContrast) FontWeight.Bold else FontWeight.Normal
                )
            }
        }
    }
}
