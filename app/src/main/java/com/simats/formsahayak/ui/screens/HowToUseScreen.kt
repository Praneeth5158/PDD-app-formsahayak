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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.formsahayak.ui.components.BottomNavigationBar

private data class HowToUseStrings(
    val title: String,
    val welcomeText: String,
    val subtitleText: String,
    val videoTutorialTitle: String,
    val videoTutorialDesc: String,
    val videoTutorialTapText: String,
    val step1Title: String,
    val step1Desc: String,
    val step2Title: String,
    val step2Desc: String,
    val step3Title: String,
    val step3Desc: String,
    val step4Title: String,
    val step4Desc: String,
    val needMoreHelpTitle: String,
    val contactSupportText: String,
    val shareExperienceTitle: String,
    val shareExperienceDesc: String,
    val giveFeedbackButtonText: String,
    val gotItButtonText: String
)

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
    val labels = when (selectedLanguage?.code) {
        "te" -> HowToUseStrings(
            title = "ఎలా ఉపయోగించాలి",
            welcomeText = "FormSahayak కి స్వాగతం!",
            subtitleText = "మీ బ్యాంక్ ఫారమ్‌లను సులభంగా నింపడానికి ఈ సాధారణ దశలను అనుసరించండి",
            videoTutorialTitle = "వీడియో ట్యుటోరియల్",
            videoTutorialDesc = "ఫారమ్ సహాయక్ ఎలా ఉపయోగించాలో తెలుసుకోవడానికి ఈ ఉపయోగకరమైన వీడియో గైడ్‌ని చూడండి.",
            videoTutorialTapText = "పూర్తి వీడియో ట్యుటోరియల్ చూడటానికి నొక్కండి",
            step1Title = "ఫారమ్ చిత్రాన్ని అప్‌లోడ్ చేయండి",
            step1Desc = "మీ బ్యాంక్ ఫారమ్ ఫోటో తీయండి లేదా గ్యాలరీ నుండి అప్‌లోడ్ చేయండి. చిత్రం స్పష్టంగా మరియు బాగా వెలిగి ఉండేలా చూసుకోండి.",
            step2Title = "యాప్ ఫీల్డ్‌లను గుర్తిస్తుంది",
            step2Desc = "ఫారమ్ సహాయక్ స్వయంచాలకంగా మీరు ఫారమ్‌లో పూరించాల్సిన అన్ని ఫీల్డ్‌లను గుర్తిస్తుంది మరియు హైలైట్ చేస్తుంది.",
            step3Title = "వాయిస్ సూచనలను అనుసరించండి",
            step3Desc = "ప్రతి ఫీల్డ్‌లో ఏమి రాయాలో వివరించే మీ భాషలోని వాయిస్ గైడెన్స్‌ని వినండి.",
            step4Title = "ఫారమ్‌ను సరిగ్గా నింపండి",
            step4Desc = "అన్ని ఫీల్డ్‌లను ఖచ్చితంగా పూరించడానికి మరియు మీ బ్యాంక్ ఫారమ్‌ను పూర్తి చేయడానికి యాప్ మార్గదర్శకత్వాన్ని ఉపయోగించండి.",
            needMoreHelpTitle = "మరింత సహాయం కావాలా?",
            contactSupportText = "సహాయం కోసం మా మద్దతు బృందాన్ని సంప్రదించండి",
            shareExperienceTitle = "మీ అనుభవాన్ని పంచుకోండి",
            shareExperienceDesc = "మీ విలువైన అభిప్రాయంతో ఫారమ్ సహాయక్ మెరుగుపరచడంలో మాకు సహాయపడండి.",
            giveFeedbackButtonText = "అభిప్రాయాన్ని తెలియజేయండి",
            gotItButtonText = "అర్థమైంది!"
        )
        "ta" -> HowToUseStrings(
            title = "எப்படி பயன்படுத்துவது",
            welcomeText = "FormSahayak-க்கு வரவேற்கிறோம்!",
            subtitleText = "உங்கள் வங்கி படிவங்களை எளிதாக நிரப்ப இந்த எளிய வழிமுறைகளை பின்பற்றவும்",
            videoTutorialTitle = "வீடியோ டுடோரியல்",
            videoTutorialDesc = "FormSahayak ஐ எவ்வாறு பயன்படுத்துவது என்பதை அறிய இந்த பயனுள்ள வீடியோ வழிகாட்டியைப் பார்க்கவும்.",
            videoTutorialTapText = "முழு வீடியோ டுடோரியலைப் பார்க்க தட்டவும்",
            step1Title = "படிவப் படத்தைப் பதிவேற்றவும்",
            step1Desc = "உங்கள் வங்கி படிவத்தின் புகைப்படத்தை எடுக்கவும் அல்லது கேலரியில் இருந்து பதிவேற்றவும். படம் தெளிவாகவும் வெளிச்சமாகவும் இருப்பதை உறுதி செய்யவும்.",
            step2Title = "ஆப் புலங்களைக் கண்டறியும்",
            step2Desc = "FormSahayak நீங்கள் படிவத்தில் நிரப்ப வேண்டிய அனைத்து புலங்களையும் தானாகவே கண்டறிந்து முன்னிலைப்படுத்தும்.",
            step3Title = "குரல் வழிமுறைகளைப் பின்பற்றவும்",
            step3Desc = "ஒவ்வொரு புலத்திலும் என்ன எழுத வேண்டும் என்பதை விளக்கும் உங்கள் மொழியில் குரல் வழிகாட்டுதலைக் கேளுங்கள்.",
            step4Title = "படிவத்தை சரியாக நிரப்பவும்",
            step4Desc = "அனைத்து புலங்களையும் துல்லியமாக நிரப்பவும் உங்கள் வங்கி படிவத்தை பூர்த்தி செய்யவும் பயன்பாட்டின் வழிகாட்டுதலைப் பயன்படுத்தவும்.",
            needMoreHelpTitle = "மேலும் உதவி வேண்டுமா?",
            contactSupportText = "உதவிக்கு எங்கள் ஆதரவு குழுவை தொடர்பு கொள்ளவும்",
            shareExperienceTitle = "உங்கள் அனுபவத்தைப் பகிரவும்",
            shareExperienceDesc = "உங்கள் மதிப்புமிக்க கருத்துக்களுடன் FormSahayak ஐ மேம்படுத்த எங்களுக்கு உதவுங்கள்.",
            giveFeedbackButtonText = "கருத்து தெரிவிக்கவும்",
            gotItButtonText = "புரிந்தது!"
        )
        else -> HowToUseStrings(
            title = "How to Use",
            welcomeText = "Welcome to FormSahayak!",
            subtitleText = "Follow these simple steps to fill your bank forms easily",
            videoTutorialTitle = "Video Tutorial",
            videoTutorialDesc = "Watch this helpful video guide to learn how to use FormSahayak.",
            videoTutorialTapText = "Tap to watch full video tutorial",
            step1Title = "Upload Form Image",
            step1Desc = "Take a photo of your bank form or upload from gallery. Make sure the image is clear and well-lit.",
            step2Title = "App Detects Fields",
            step2Desc = "FormSahayak automatically identifies and highlights all the fields you need to fill in the form.",
            step3Title = "Follow Voice Instructions",
            step3Desc = "Listen to voice guidance in your language that explains what to write in each field.",
            step4Title = "Fill Form Correctly",
            step4Desc = "Use the app's guidance to fill all fields accurately and complete your bank form.",
            needMoreHelpTitle = "Need More Help?",
            contactSupportText = "Contact our support team for assistance",
            shareExperienceTitle = "Share Your Experience",
            shareExperienceDesc = "Help us improve FormSahayak with your valuable feedback.",
            giveFeedbackButtonText = "Give Feedback",
            gotItButtonText = "Got It!"
        )
    }

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
                        labels.title, 
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
                        text = labels.welcomeText,
                        fontSize = 20.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = actualTextColor,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = labels.subtitleText,
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
                            text = labels.videoTutorialTitle,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = actualTextColor
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = labels.videoTutorialDesc,
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
                        text = labels.videoTutorialTapText,
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
                title = labels.step1Title,
                description = labels.step1Desc,
                icon = Icons.Default.PhotoCamera,
                iconColor = if (isHighContrast) Color.White else Color(0xFF2196F3),
                bgColor = if (isHighContrast) Color.Black else Color(0xFFE3F2FD),
                textColor = actualTextColor,
                isDark = isDarkMode,
                isHighContrast = isHighContrast
            )

            HowToUseStepItem(
                number = 2,
                title = labels.step2Title,
                description = labels.step2Desc,
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
                title = labels.step3Title,
                description = labels.step3Desc,
                icon = Icons.AutoMirrored.Filled.VolumeUp,
                iconColor = if (isHighContrast) Color.White else Color(0xFF9C27B0),
                bgColor = if (isHighContrast) Color.Black else Color(0xFFF3E5F5),
                textColor = actualTextColor,
                isDark = isDarkMode,
                isHighContrast = isHighContrast
            )

            HowToUseStepItem(
                number = 4,
                title = labels.step4Title,
                description = labels.step4Desc,
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
                            text = labels.needMoreHelpTitle,
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = labels.contactSupportText,
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
                        text = labels.shareExperienceTitle,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = actualTextColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = labels.shareExperienceDesc,
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
                                Text(labels.giveFeedbackButtonText, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
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
                Text(labels.gotItButtonText, fontSize = 18.sp, fontWeight = FontWeight.ExtraBold, color = Color.White)
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
