package com.simats.formsahayak.ui.screens

import android.widget.Toast
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.formsahayak.R
import com.simats.formsahayak.ui.viewmodel.FormViewModel

@Composable
fun FeedbackScreen(
    selectedLanguage: Language?,
    viewModel: FormViewModel,
    onFinished: () -> Unit,
    onCancel: () -> Unit
) {
    val context = LocalContext.current
    
    val optionsEase = listOf(
        stringResource(R.string.ease_very_easy),
        stringResource(R.string.ease_easy),
        stringResource(R.string.ease_moderate),
        stringResource(R.string.ease_difficult)
    )
    val optionsVoice = listOf(
        stringResource(R.string.voice_very_helpful),
        stringResource(R.string.voice_helpful),
        stringResource(R.string.voice_somewhat_helpful),
        stringResource(R.string.voice_not_helpful)
    )
    val optionsRecommend = listOf(
        stringResource(R.string.rec_yes),
        stringResource(R.string.rec_probably),
        stringResource(R.string.rec_maybe),
        stringResource(R.string.rec_no)
    )

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
                title = stringResource(R.string.q1_title),
                desc = stringResource(R.string.q1_desc),
                icon = Icons.Default.Star,
                iconColor = Color(0xFFFFB300),
                onNext = { currentStep = 2 },
                onBack = onCancel,
                isNextEnabled = rating > 0,
                isLoading = viewModel.isLoading
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
                    Text(stringResource(R.string.q1_hint), fontSize = 14.sp, color = Color.Gray)
                    if (rating == 0) {
                        Text(stringResource(R.string.rating_warning), fontSize = 12.sp, color = Color.Red.copy(alpha = 0.7f), modifier = Modifier.padding(top = 8.dp))
                    }
                }
            }
            2 -> QuestionLayout(
                step = 2,
                totalSteps = 5,
                title = stringResource(R.string.q2_title),
                desc = stringResource(R.string.q2_desc),
                icon = Icons.Default.TouchApp,
                iconColor = Color(0xFF2196F3),
                onNext = { currentStep = 3 },
                onBack = { currentStep = 1 },
                isNextEnabled = easeSelection != -1,
                isLoading = viewModel.isLoading
            ) {
                SelectionOptions(
                    options = optionsEase,
                    selectedIndex = easeSelection,
                    onSelect = { easeSelection = it },
                    icons = listOf("😊", "🙂", "😐", "😕"),
                    iconColors = listOf(Color(0xFFFFD54F), Color(0xFFFFD54F), Color(0xFFFFD54F), Color(0xFFFFD54F))
                )
            }
            3 -> QuestionLayout(
                step = 3,
                totalSteps = 5,
                title = stringResource(R.string.q3_title),
                desc = stringResource(R.string.q3_desc),
                icon = Icons.Default.GraphicEq,
                iconColor = Color(0xFF4CAF50),
                onNext = { currentStep = 4 },
                onBack = { currentStep = 2 },
                isNextEnabled = voiceSelection != -1,
                isLoading = viewModel.isLoading
            ) {
                SelectionOptions(
                    options = optionsVoice,
                    selectedIndex = voiceSelection,
                    onSelect = { voiceSelection = it },
                    vectorIcons = listOf(Icons.Default.AutoFixHigh, Icons.Default.Check, Icons.Default.Info, Icons.Default.Block),
                    iconColors = listOf(Color.Black, Color(0xFF4CAF50), Color(0xFF2196F3), Color(0xFFF44336))
                )
            }
            4 -> QuestionLayout(
                step = 4,
                totalSteps = 5,
                title = stringResource(R.string.q4_title),
                desc = stringResource(R.string.q4_desc),
                icon = Icons.Default.ThumbUp,
                iconColor = Color(0xFF9C27B0),
                onNext = { currentStep = 5 },
                onBack = { currentStep = 3 },
                isNextEnabled = recommendSelection != -1,
                isLoading = viewModel.isLoading
            ) {
                SelectionOptions(
                    options = optionsRecommend,
                    selectedIndex = recommendSelection,
                    onSelect = { recommendSelection = it },
                    icons = listOf("👍", "✅", "🤔", "👎"),
                    iconColors = listOf(Color(0xFF4CAF50), Color(0xFF4CAF50), Color(0xFFFFB300), Color(0xFFF44336))
                )
            }
            5 -> QuestionLayout(
                step = 5,
                totalSteps = 5,
                title = stringResource(R.string.q5_title),
                desc = stringResource(R.string.q5_desc),
                icon = Icons.Default.ChatBubbleOutline,
                iconColor = Color(0xFFE91E63),
                onNext = {
                    val exp = "${rating} Stars - ${optionsEase.getOrNull(easeSelection) ?: ""}"
                    val vHelp = optionsVoice.getOrNull(voiceSelection) ?: ""
                    val rec = optionsRecommend.getOrNull(recommendSelection) ?: ""
                    
                    viewModel.submitFeedback(exp, vHelp, rec, comments) { success, msg ->
                        if (success) {
                            currentStep = 6
                        } else {
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                },
                onBack = { currentStep = 4 },
                isNextEnabled = true,
                isLastStep = true,
                isLoading = viewModel.isLoading
            ) {
                Column {
                    Text(stringResource(R.string.q5_hint), fontWeight = FontWeight.Bold, fontSize = 14.sp, modifier = Modifier.padding(bottom = 8.dp))
                    OutlinedTextField(
                        value = comments,
                        onValueChange = { if (it.length <= 500) comments = it },
                        modifier = Modifier.fillMaxWidth().height(150.dp),
                        placeholder = { Text(stringResource(R.string.q5_hint)) },
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
            6 -> SuccessLayout(onFinished, onReset = {
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
    icon: ImageVector,
    iconColor: Color,
    onNext: () -> Unit,
    onBack: () -> Unit,
    isNextEnabled: Boolean,
    isLastStep: Boolean = false,
    isLoading: Boolean = false,
    content: @Composable () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp).verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween, verticalAlignment = Alignment.CenterVertically) {
            Text(text = "${stringResource(R.string.question_label)} $step ${stringResource(R.string.of_label)} $totalSteps", color = Color(0xFF1976D2), fontWeight = FontWeight.Bold, fontSize = 14.sp)
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
            enabled = isNextEnabled && !isLoading,
            modifier = Modifier.fillMaxWidth().height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isLastStep) Color(0xFF2E7D32) else Color(0xFF1976D2),
                disabledContainerColor = Color(0xFFF3F4F6)
            )
        ) {
            if (isLoading) {
                CircularProgressIndicator(modifier = Modifier.size(24.dp), color = Color.White, strokeWidth = 2.dp)
            } else {
                Text(text = if (isLastStep) stringResource(R.string.submit_feedback) else stringResource(R.string.next_q), fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        TextButton(onClick = onBack, enabled = !isLoading) {
            Text(text = if (step == 1) stringResource(R.string.cancel) else stringResource(R.string.prev_q), color = if (step == 1) Color.Red else Color.Gray, fontWeight = FontWeight.Bold)
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
        Text(stringResource(R.string.feedback_success), fontSize = 28.sp, fontWeight = FontWeight.Bold, color = Color.Black)
        Text(stringResource(R.string.feedback_submitted), fontSize = 16.sp, color = Color.Gray)

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
                    Text(stringResource(R.string.feedback_success), fontWeight = FontWeight.Bold, color = Color(0xFF1976D2), fontSize = 18.sp)
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(stringResource(R.string.feedback_appreciation), fontSize = 14.sp, color = Color(0xFF1976D2), lineHeight = 22.sp)
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
                Text(stringResource(R.string.feedback_recorded), color = Color(0xFF2E7D32), fontSize = 12.sp, fontWeight = FontWeight.Medium)
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
            Text(stringResource(R.string.back_to_home), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
        }

        Spacer(modifier = Modifier.height(16.dp))
        TextButton(onClick = onReset) {
            Text(stringResource(R.string.submit_another), color = Color(0xFF1976D2), fontWeight = FontWeight.SemiBold)
        }
    }
}
