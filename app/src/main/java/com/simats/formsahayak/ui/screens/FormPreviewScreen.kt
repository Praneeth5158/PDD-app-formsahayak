package com.simats.formsahayak.ui.screens

import android.graphics.Bitmap
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Stars
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.formsahayak.ui.viewmodel.FormViewModel
import kotlin.math.min

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormPreviewScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    viewModel: FormViewModel,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit,
    onContinueClick: () -> Unit,
    onRetakeClick: () -> Unit
) {
    val context = LocalContext.current
    val isDark = isDarkMode || isHighContrast
    val backgroundColor = if (isDark) Color.Black else Color(0xFFF0F4F8)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color(0xFF1A237E)

    val formImage = viewModel.capturedBitmap
    val detectedFields = viewModel.detectedFields
    
    var currentFieldIndex by remember { mutableStateOf(0) }
    var showInitialAnimation by remember { mutableStateOf(true) }

    // Animation for the "Scan" line
    val infiniteTransition = rememberInfiniteTransition(label = "scanLine")
    val scanLineY = infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(2000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "scanLineY"
    )

    // Animation for the box pulse
    val boxPulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "boxPulse"
    )

    // Auto-speak instructions when field changes
    LaunchedEffect(currentFieldIndex) {
        if (detectedFields.isNotEmpty()) {
            val field = detectedFields[currentFieldIndex]
            viewModel.speakFieldInstruction(field, selectedLanguage?.code ?: "en")
        }
    }

    LaunchedEffect(Unit) {
        kotlinx.coroutines.delay(2500)
        showInitialAnimation = false
    }

    // Translation logic
    val title = when (selectedLanguage?.code) {
        "te" -> "ఫారమ్ ప్రివ్యూ"
        "ta" -> "படிவ முன்னோட்டம்"
        else -> "Form Guide"
    }
    val nextText = when (selectedLanguage?.code) {
        "te" -> "తదుపరి"
        "ta" -> "அடுத்து"
        else -> "Next"
    }
    val prevText = when (selectedLanguage?.code) {
        "te" -> "మునుపటి"
        "ta" -> "முந்தைய"
        else -> "Previous"
    }
    val finishText = when (selectedLanguage?.code) {
        "te" -> "పూర్తయింది"
        "ta" -> "முடிந்தது"
        else -> "Finish"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Stars, contentDescription = null, tint = Color(0xFFFFD600), modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(title, fontWeight = FontWeight.ExtraBold, color = textColor)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = textColor)
                    }
                },
                actions = {
                    IconButton(onClick = onHomeClick) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = "Home", tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor)
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Field Info Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = cardColor),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Surface(
                        modifier = Modifier.size(48.dp),
                        shape = CircleShape,
                        color = Color(0xFFE8EAF6)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            Text(
                                text = "${currentFieldIndex + 1}",
                                fontWeight = FontWeight.ExtraBold,
                                color = Color(0xFF3F51B5),
                                fontSize = 20.sp
                            )
                        }
                    }
                    Spacer(Modifier.width(16.dp))
                    Column {
                        Text(
                            text = if (selectedLanguage?.code == "te") "ఫీల్డ్ పేరు" else "Current Field",
                            fontSize = 12.sp,
                            color = if (isDark) Color.LightGray else Color.Gray
                        )
                        if (detectedFields.isNotEmpty()) {
                            Text(
                                text = detectedFields[currentFieldIndex].name,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Main Image with Interactive Overlay
            Surface(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(400.dp),
                shape = RoundedCornerShape(20.dp),
                color = Color.Black,
                shadowElevation = if (isHighContrast) 0.dp else 4.dp,
                border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
            ) {
                Box(modifier = Modifier.fillMaxSize()) {
                    if (formImage != null) {
                        Image(
                            bitmap = formImage.asImageBitmap(),
                            contentDescription = "Form",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                        
                        Canvas(modifier = Modifier.fillMaxSize()) {
                            if (detectedFields.isNotEmpty()) {
                                val field = detectedFields[currentFieldIndex]
                                
                                val bitmapWidth = formImage.width.toFloat()
                                val bitmapHeight = formImage.height.toFloat()
                                val canvasWidth = size.width
                                val canvasHeight = size.height

                                val scale = min(canvasWidth / bitmapWidth, canvasHeight / bitmapHeight)
                                val offsetX = (canvasWidth - bitmapWidth * scale) / 2f
                                val offsetY = (canvasHeight - bitmapHeight * scale) / 2f

                                // Draw the box for the CURRENT field
                                val rectWidth = field.bounds.width() * scale
                                val rectHeight = field.bounds.height() * scale
                                val rectLeft = field.bounds.left * scale + offsetX
                                val rectTop = field.bounds.top * scale + offsetY

                                // Pulsing red box
                                drawRect(
                                    color = Color.Red,
                                    topLeft = Offset(rectLeft, rectTop),
                                    size = Size(rectWidth, rectHeight),
                                    style = Stroke(width = 8f * boxPulseScale)
                                )
                            }

                            // Scanning line animation (shown initially or always)
                            if (showInitialAnimation) {
                                val lineY = size.height * scanLineY.value
                                drawRect(
                                    brush = Brush.verticalGradient(
                                        colors = listOf(
                                            Color.Green.copy(alpha = 0f),
                                            Color.Green.copy(alpha = 0.5f),
                                            Color.Green.copy(alpha = 0f)
                                        ),
                                        startY = lineY - 20,
                                        endY = lineY + 20
                                    ),
                                    topLeft = Offset(0f, lineY - 20),
                                    size = Size(size.width, 40f)
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // AI Guidance Card from Backend
            if (viewModel.isLoading) {
                LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                Text("AI Analyzing...", fontSize = 12.sp, color = textColor)
            } else if (viewModel.backendGuidance.isNotEmpty()) {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isDark) Color(0xFF2C2C2C) else Color(0xFFE8EAF6)
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Stars,
                                contentDescription = null,
                                tint = Color(0xFFFFD600),
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(Modifier.width(8.dp))
                            Text(
                                text = if (selectedLanguage?.code == "te") "AI మార్గదర్శకత్వం" else if (selectedLanguage?.code == "ta") "AI வழிகாட்டுதல்" else "AI Guidance",
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(Modifier.height(8.dp))
                        Text(
                            text = viewModel.backendGuidance,
                            fontSize = 13.sp,
                            color = textColor.copy(alpha = 0.8f),
                            lineHeight = 18.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Navigation Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                OutlinedButton(
                    onClick = { if (currentFieldIndex > 0) currentFieldIndex-- },
                    enabled = currentFieldIndex > 0,
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFF3F51B5))
                ) {
                    Icon(Icons.AutoMirrored.Filled.ArrowBack, null)
                    Spacer(Modifier.width(4.dp))
                    Text(prevText, fontWeight = FontWeight.Bold)
                }
                
                Button(
                    onClick = { 
                        if (currentFieldIndex < detectedFields.size - 1) {
                            currentFieldIndex++ 
                        } else {
                            onContinueClick()
                        }
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (currentFieldIndex == detectedFields.size - 1) Color(0xFF4CAF50) else Color(0xFF2196F3)
                    )
                ) {
                    Text(if (currentFieldIndex == detectedFields.size - 1) finishText else nextText, fontWeight = FontWeight.Bold)
                    Spacer(Modifier.width(4.dp))
                    Icon(if (currentFieldIndex == detectedFields.size - 1) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward, null)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Repeat Voice Instruction
            Button(
                onClick = { 
                    if (detectedFields.isNotEmpty()) {
                        viewModel.speakFieldInstruction(detectedFields[currentFieldIndex], selectedLanguage?.code ?: "en")
                    } else if (viewModel.backendGuidance.isNotEmpty()) {
                        viewModel.speak(viewModel.backendGuidance, "en")
                    }
                },
                modifier = Modifier.fillMaxWidth().height(48.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7986CB)),
                shape = RoundedCornerShape(12.dp)
            ) {
                Icon(Icons.AutoMirrored.Filled.VolumeUp, null)
                Spacer(Modifier.width(8.dp))
                Text(if (selectedLanguage?.code == "te") "సూచన మళ్ళీ వినండి" else "Listen Again", fontWeight = FontWeight.Medium)
            }
        }
    }
}
