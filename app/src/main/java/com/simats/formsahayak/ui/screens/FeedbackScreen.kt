package com.simats.formsahayak.ui.screens

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private data class FeedbackStrings(
    val stepLabel: String,
    val ofLabel: String,
    val cancel: String,
    val next: String,
    val previous: String,
    val submit: String,
    val backToHome: String,
    val q1Title: String,
    val q1Desc: String,
    val q1Hint: String,
    val q2Title: String,
    val q2Desc: String,
    val q3Title: String,
    val q3Desc: String,
    val q4Title: String,
    val q4Desc: String,
    val q5Title: String,
    val q5Desc: String,
    val q5Hint: String,
    val successTitle: String,
    val successSubtitle: String,
    val thankYouTitle: String,
    val successMessage: String,
    val successSubmitAnother: String,
    val optionsEase: List<String>,
    val optionsVoice: List<String>,
    val optionsRecommend: List<String>,
    val placeholderText: String,
    val ratingWarning: String
)

@Composable
fun FeedbackScreen(
    selectedLanguage: Language?,
    onFinished: () -> Unit,
    onCancel: () -> Unit
) {
    val labels = remember(selectedLanguage) {
        when (selectedLanguage?.code) {
            "te" -> FeedbackStrings(
                stepLabel = "ప్రశ్న",
                ofLabel = "/",
                cancel = "రద్దు చేయి",
                next = "తదుపరి ప్రశ్న →",
                previous = "మునుపటి ప్రశ్న",
                submit = "అభిప్రాయాన్ని పంపండి",
                backToHome = "హోమ్ పేజీకి వెళ్ళండి",
                q1Title = "మీ మొత్తం అనుభవాన్ని ఎలా రేట్ చేస్తారు?",
                q1Desc = "దయచేసి మీ అనుభవానికి సరిపోయే నక్షత్రాల సంఖ్యను ఎంచుకోండి.",
                q1Hint = "రేట్ చేయడానికి నక్షత్రాలను నొక్కండి",
                q2Title = "యాప్‌ను ఉపయోగించడం ఎంత సులభం?",
                q2Desc = "మీ అనుభవాన్ని ఉత్తమంగా వివరించే ఎంపికను ఎంచుకోండి.",
                q3Title = "వాయిస్ గైడెన్స్ ఎంత సహాయకరంగా ఉంది?",
                q3Desc = "వాయిస్ గైడెన్స్ ఫీచర్‌తో మీ అనుభవాన్ని వివరించండి.",
                q4Title = "మీరు FormSahayakను ఇతరులకు సిఫార్సు చేస్తారా?",
                q4Desc = "మీ స్నేహితులు మరియు కుటుంబ సభ్యులకు ఈ యాప్‌ను సూచిస్తారో లేదோ మాకు తెలియజేయండి.",
                q5Title = "అదనపు వ్యాఖ్యలు",
                q5Desc = "ఏవైనా అదనపు ఆలోచనలు లేదా సూచనలను పంచుకోండి (ఐచ్ఛికం).",
                q5Hint = "మీకు నచ్చినవి లేదా మేము మెరుగుపరచగలవి మాకు తెలియజేయండి...",
                successTitle = "ధన్యవాదాలు!",
                successSubtitle = "మీ అభిప్రాయం సమర్పించబడింది",
                thankYouTitle = "ధన్యవాదాలు!",
                successMessage = "మీ ఆలోచనలను పంచుకోవడానికి సమయం కేటాయించినందుకు మేము మిమ్మల్ని అభినందిస్తున్నాము. మీ అభిప్రాయం FormSahayakను అందరికీ మెరుగ్గా చేయడంలో సహాయపడుతుంది.",
                successSubmitAnother = "మరొక అభిప్రాయాన్ని సమర్పించండి",
                optionsEase = listOf("చాలా సులభం", "సులభం", "మధ్యస్థం", "కష్టం"),
                optionsVoice = listOf("చాలా సహాయకరం", "సహాయకరం", "కొంచెం సహాయకరం", "సహాయకరం కాదు"),
                optionsRecommend = listOf("తప్పకుండా అవును", "బహుశా అవును", "కావచ్చు", "లేదు"),
                placeholderText = "మీ అభిప్రాయం",
                ratingWarning = "కొనసాగడానికి దయచేసి రేటింగ్‌ను ఎంచుకోండి"
            )
            "ta" -> FeedbackStrings(
                stepLabel = "கேள்வி",
                ofLabel = "/",
                cancel = "ரத்துசெய்",
                next = "அடுத்த கேள்வி →",
                previous = "முந்தைய கேள்வி",
                submit = "கருத்தைச் சமர்ப்பிக்கவும்",
                backToHome = "முகப்புப் பக்கத்திற்குச் செல்லவும்",
                q1Title = "உங்கள் ஒட்டுமொத்த அனுபவத்தை எப்படி மதிப்பிடுவீர்கள்?",
                q1Desc = "உங்கள் அனுபவத்திற்குப் பொருத்தமான நட்சத்திரங்களின் எண்ணிக்கையைத் தேர்ந்தெடுக்கவும்.",
                q1Hint = "மதிப்பிட நட்சத்திரங்களைத் தட்டவும்",
                q2Title = "இந்தச் செயலியைப் பயன்படுத்துவது எவ்வளவு எளிது?",
                q2Desc = "உங்கள் அனுபவத்தை சிறப்பாக விளக்கும் விருப்பத்தைத் தேர்ந்தெடுக்கவும்.",
                q3Title = "குரல் வழிகாட்டுதல் எவ்வளவு உதவியாக இருந்தது?",
                q3Desc = "குரல் வழிகாட்டுதல் அம்சத்தைப் பற்றிய உங்கள் அனுபவத்தை விவரிக்கவும்.",
                q4Title = "FormSahayak-ஐ மற்றவர்களுக்குப் பரிந்துரைப்பீர்களா?",
                q4Desc = "உங்கள் நண்பர்கள் மற்றும் குடும்பத்தினருக்கு இந்தச் செயலியைப் பரிந்துரைப்பீர்களா என்பதை எங்களுக்குத் தெரிவிக்கவும்.",
                q5Title = "கூடுதல் கருத்துகள்",
                q5Desc = "கூடுதல் எண்ணங்கள் அல்லது பரிந்துரைகளைப் பகிரவும் (விருப்பத்தேர்வு).",
                q5Hint = "நீங்கள் விரும்புவதை அல்லது நாங்கள் மேம்படுத்த வேண்டியதை எங்களுக்குத் தெரிவிக்கவும்...",
                successTitle = "நன்றி!",
                successSubtitle = "உங்கள் கருத்து சமர்ப்பிக்கப்பட்டது",
                thankYouTitle = "நன்றி!",
                successMessage = "உங்கள் கருத்துக்களைப் பகிர நேரம் ஒதுக்கியமைக்கு நாங்கள் உங்களைப் பாராட்டுகிறோம். உங்கள் கருத்து FormSahayak-ஐ அனைவருக்கும் சிறந்ததாக மாற்ற உதவும்.",
                successSubmitAnother = "மற்றொரு கருத்தைச் சமர்ப்பிக்கவும்",
                optionsEase = listOf("மிகவும் எளிது", "எளிது", "மிதமானது", "கடினம்"),
                optionsVoice = listOf("மிகவும் உதவியாக இருந்தது", "உதவியாக இருந்தது", "ஓரளவு உதவியாக இருந்தது", "உதவியாக இல்லை"),
                optionsRecommend = listOf("நிச்சயமாக ஆம்", "அநேகமாக ஆம்", "இருக்கலாம்", "இல்லை"),
                placeholderText = "உங்கள் கருத்து",
                ratingWarning = "தொடர மதிப்பீட்டைத் தேர்ந்தெடுக்கவும்"
            )
            else -> FeedbackStrings(
                stepLabel = "Question",
                ofLabel = "of",
                cancel = "Cancel",
                next = "Next Question →",
                previous = "← Previous Question",
                submit = "Submit Feedback",
                backToHome = "Go to Home",
                q1Title = "How would you rate your overall experience?",
                q1Desc = "Please select the number of stars that match your experience.",
                q1Hint = "Tap stars to rate",
                q2Title = "How easy is the app to use?",
                q2Desc = "Select the option that best describes your experience.",
                q3Title = "How helpful is the voice guidance?",
                q3Desc = "What describe your experience with the voice guidance feature.",
                q4Title = "Would you recommend FormSahayak to others?",
                q4Desc = "Help us know if you'd suggest this app to your friends and family.",
                q5Title = "Additional Comments",
                q5Desc = "Share any additional thoughts or suggestions (Optional).",
                q5Hint = "Tell us what you liked or what we can improve...",
                successTitle = "Thank You!",
                successSubtitle = "Your Feedback Has Been Submitted",
                thankYouTitle = "Thank You!",
                successMessage = "We truly appreciate you taking the time to share your thoughts. Your feedback helps us make FormSahayak better for everyone.",
                successSubmitAnother = "Submit another feedback",
                optionsEase = listOf("Very Easy", "Easy", "Moderate", "Difficult"),
                optionsVoice = listOf("Very Helpful", "Helpful", "Somewhat Helpful", "Not Helpful"),
                optionsRecommend = listOf("Yes, Definitely", "Probably Yes", "Maybe", "No"),
                placeholderText = "Your Feedback",
                ratingWarning = "Please select a rating to continue"
            )
        }
    }

    var currentStep by remember { mutableStateOf(1) }
    var rating by remember { mutableStateOf(0) }
    var easeSelection by remember { mutableStateOf(-1) }
    var voiceSelection by remember { mutableStateOf(-1) }
    var recommendSelection by remember { mutableStateOf(-1) }
    var comments by remember { mutableStateOf("") }

    Crossfade(targetState = currentStep, label = "FeedbackTransition") { step ->
        when (step) {
            1 -> QuestionLayout(
                step = 1,
                totalSteps = 5,
                title = labels.q1Title,
                desc = labels.q1Desc,
                labels = labels,
                icon = Icons.Default.Star,
                iconColor = Color(0xFFFFB300),
                onNext = { currentStep = 2 },
                onBack = onCancel,
                isNextEnabled = rating > 0
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        repeat(5) { index ->
                            val starIndex = index + 1
                            IconButton(onClick = { rating = starIndex }) {
                                Icon(
                                    imageVector = if (starIndex <= rating) Icons.Filled.Star else Icons.Outlined.StarOutline,
                                    contentDescription = null,
                                    tint = if (starIndex <= rating) Color(0xFFFFB300) else Color.LightGray,
                                    modifier = Modifier.size(42.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(labels.q1Hint, fontSize = 14.sp, color = Color.Gray)
                    if (rating == 0) {
                        Text(labels.ratingWarning, fontSize = 12.sp, color = Color.Red.copy(alpha = 0.7f), modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
            2 -> QuestionLayout(
                step = 2,
                totalSteps = 5,
                title = labels.q2Title,
                desc = labels.q2Desc,
                labels = labels,
                icon = Icons.Default.TouchApp,
                iconColor = Color(0xFF2196F3),
                onNext = { currentStep = 3 },
                onBack = { currentStep = 1 },
                isNextEnabled = easeSelection != -1
            ) {
                SelectionOptions(
                    options = labels.optionsEase,
                    selectedIndex = easeSelection,
                    onSelect = { easeSelection = it },
                    icons = listOf("😊", "🙂", "😐", "😕"),
                    iconColors = listOf(Color(0xFFFFD54F), Color(0xFFFFD54F), Color(0xFFFFD54F), Color(0xFFFFD54F))
                )
            }
            3 -> QuestionLayout(
                step = 3,
                totalSteps = 5,
                title = labels.q3Title,
                desc = labels.q3Desc,
                labels = labels,
                icon = Icons.Default.GraphicEq,
                iconColor = Color(0xFF4CAF50),
                onNext = { currentStep = 4 },
                onBack = { currentStep = 2 },
                isNextEnabled = voiceSelection != -1
            ) {
                SelectionOptions(
                    options = labels.optionsVoice,
                    selectedIndex = voiceSelection,
                    onSelect = { voiceSelection = it },
                    vectorIcons = listOf(Icons.Default.AutoFixHigh, Icons.Default.Check, Icons.Default.Info, Icons.Default.Block),
                    iconColors = listOf(Color.Black, Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFF44336))
                )
            }
            4 -> QuestionLayout(
                step = 4,
                totalSteps = 5,
                title = labels.q4Title,
                desc = labels.q4Desc,
                labels = labels,
                icon = Icons.Default.ThumbUp,
                iconColor = Color(0xFF9C27B0),
                onNext = { currentStep = 5 },
                onBack = { currentStep = 3 },
                isNextEnabled = recommendSelection != -1
            ) {
                SelectionOptions(
                    options = labels.optionsRecommend,
                    selectedIndex = recommendSelection,
                    onSelect = { recommendSelection = it },
                    icons = listOf("👍", "✅", "🤔", "👎"),
                    iconColors = listOf(Color(0xFF4CAF50), Color(0xFF4CAF50), Color(0xFFFFB300), Color(0xFFF44336))
                )
            }
            5 -> QuestionLayout(
                step = 5,
                totalSteps = 5,
                title = labels.q5Title,
                desc = labels.q5Desc,
                labels = labels,
                icon = Icons.Default.ChatBubbleOutline,
                iconColor = Color(0xFFE91E63),
                onNext = { currentStep = 6 },
                onBack = { currentStep = 4 },
                isNextEnabled = true,
                isLastStep = true
            ) {
                Column {
                    Text(labels.placeholderText, fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
                    OutlinedTextField(
                        value = comments,
                        onValueChange = { if (it.length <= 500) comments = it },
                        modifier = Modifier.fillMaxWidth().height(150.dp),
                        placeholder = { Text("Tell us what you liked or what we can improve...\n\nExample:\n- The voice guidance was very helpful\n- The UI is easy to navigate") },
                        shape = RoundedCornerShape(12.dp)
                    )
                    Text(
                        "${comments.length}/500",
                        modifier = Modifier.align(Alignment.End).padding(top = 4.dp),
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }
            }
            6 -> SuccessLayout(labels, onFinished, onReset = {
                currentStep = 1
                rating = 0
                easeSelection = -1
                voiceSelection = -1
                recommendSelection = -1
                comments = ""
            })
        }
    }
}

@Composable
private fun QuestionLayout(
    step: Int,
    totalSteps: Int,
    title: String,
    desc: String,
    labels: FeedbackStrings,
    icon: ImageVector,
    iconColor: Color,
    onNext: () -> Unit,
    onBack: () -> Unit,
    isNextEnabled: Boolean,
    isLastStep: Boolean = false,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "${labels.stepLabel} $step ${labels.ofLabel} $totalSteps", color = Color(0xFF1976D2), fontWeight = FontWeight.Bold, fontSize = 14.sp)
            val progress = step.toFloat() / totalSteps
            val animatedProgress by animateFloatAsState(targetValue = progress, label = "Progress")
            LinearProgressIndicator(
                progress = { animatedProgress },
                modifier = Modifier.width(100.dp).height(8.dp).background(Color(0xFFE3F2FD), CircleShape),
                color = Color(0xFF1976D2),
                strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
            )
            Text(text = "${(progress * 100).toInt()}%", color = Color(0xFF1976D2), fontWeight = FontWeight.Bold, fontSize = 14.sp)
        }

        Spacer(modifier = Modifier.height(40.dp))
        
        Surface(modifier = Modifier.size(60.dp), shape = CircleShape, color = iconColor.copy(alpha = 0.1f)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(30.dp))
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        Text(text = title, fontSize = 22.sp, fontWeight = FontWeight.Bold, textAlign = TextAlign.Center, color = Color.Black)
        Spacer(modifier = Modifier.height(12.dp))
        Text(text = desc, fontSize = 14.sp, color = Color.Gray, textAlign = TextAlign.Center, modifier = Modifier.padding(horizontal = 16.dp))
        Spacer(modifier = Modifier.height(32.dp))
        
        content()

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onNext,
            enabled = isNextEnabled,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isLastStep) Color(0xFF2E7D32) else Color(0xFF1976D2),
                disabledContainerColor = Color(0xFFF3F4F6)
            )
        ) {
            Text(text = if (isLastStep) labels.submit else labels.next, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = onBack) {
            Text(text = if (step == 1) labels.cancel else labels.previous, color = if (step == 1) Color.Red else Color.Gray, fontWeight = FontWeight.Bold)
        }
    }
}

@Composable
private fun SelectionOptions(
    options: List<String>,
    selectedIndex: Int,
    onSelect: (Int) -> Unit,
    icons: List<String>? = null,
    vectorIcons: List<ImageVector>? = null,
    iconColors: List<Color>? = null
) {
    Column(verticalArrangement = Arrangement.spacedBy(12.dp)) {
        options.forEachIndexed { index, option ->
            val isSelected = selectedIndex == index
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(index) },
                shape = RoundedCornerShape(12.dp),
                color = if (isSelected) Color(0xFFE8F5E9) else Color.White,
                border = BorderStroke(1.dp, if (isSelected) Color(0xFF2E7D32) else Color(0xFFEEEEEE))
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (icons != null) {
                        Text(text = icons[index], fontSize = 20.sp)
                    } else if (vectorIcons != null) {
                        Icon(
                            imageVector = vectorIcons[index],
                            contentDescription = null,
                            tint = iconColors?.get(index) ?: Color.Gray,
                            modifier = Modifier.size(24.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Text(
                        text = option,
                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                        color = Color.Black,
                        modifier = Modifier.weight(1f)
                    )
                    RadioButton(
                        selected = isSelected,
                        onClick = { onSelect(index) },
                        colors = RadioButtonDefaults.colors(selectedColor = Color(0xFF2E7D32))
                    )
                }
            }
        }
    }
}

@Composable
private fun SuccessLayout(
    labels: FeedbackStrings,
    onFinished: () -> Unit,
    onReset: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))
        
        Surface(modifier = Modifier.size(90.dp), shape = CircleShape, color = Color(0xFFE8F5E9)) {
            Box(contentAlignment = Alignment.Center) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(50.dp))
            }
        }

        Spacer(modifier = Modifier.height(32.dp))
        Text(labels.successTitle, fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(labels.successSubtitle, fontSize = 16.sp, color = Color.Gray)

        Spacer(modifier = Modifier.height(32.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = Color(0xFFF1F6FF),
            border = BorderStroke(1.dp, Color(0xFFD1E3FF))
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.ChatBubbleOutline, contentDescription = null, tint = Color(0xFF1976D2), modifier = Modifier.size(24.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(labels.thankYouTitle, fontWeight = FontWeight.Bold, color = Color(0xFF1976D2), fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(labels.successMessage, fontSize = 14.sp, color = Color(0xFF1976D2), lineHeight = 22.sp)
            }
        }

        Spacer(modifier = Modifier.height(24.dp))
        
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = Color(0xFFE8F5E9),
            border = BorderStroke(1.dp, Color(0xFFC8E6C9))
        ) {
            Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF2E7D32), modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(12.dp))
                Text("Feedback received and recorded successfully", color = Color(0xFF2E7D32), fontSize = 12.sp, fontWeight = FontWeight.Medium)
            }
        }

        Spacer(modifier = Modifier.weight(1f))
        Spacer(modifier = Modifier.height(32.dp))

        Button(
            onClick = onFinished,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF1976D2))
        ) {
            Text(labels.backToHome, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onReset) {
            Text(labels.successSubmitAnother, color = Color(0xFF1976D2), fontWeight = FontWeight.SemiBold)
        }
    }
}
