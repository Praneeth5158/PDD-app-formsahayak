package com.simats.formsahayak.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.*
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
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.simats.formsahayak.R
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

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            if (detectedFields.isNotEmpty()) {
                viewModel.speakFieldInstruction(detectedFields[currentFieldIndex], selectedLanguage?.code ?: "en")
            } else if (viewModel.backendGuidance.isNotEmpty()) {
                viewModel.speak(viewModel.backendGuidance, selectedLanguage?.code ?: "en")
            }
        } else {
            Toast.makeText(context, context.getString(R.string.audio_denied), Toast.LENGTH_SHORT).show()
        }
    }

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

    // Animation for the box pulse alpha
    val boxAlpha by infiniteTransition.animateFloat(
        initialValue = 0.4f,
        targetValue = 1.0f,
        animationSpec = infiniteRepeatable(
            animation = tween(1000, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "boxAlpha"
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

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.Stars, contentDescription = null, tint = Color(0xFFFFD600), modifier = Modifier.size(20.dp))
                        Spacer(Modifier.width(8.dp))
                        Text(stringResource(R.string.form_guide), fontWeight = FontWeight.ExtraBold, color = textColor)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back), tint = textColor)
                    }
                },
                actions = {
                    IconButton(onClick = onHomeClick) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = stringResource(R.string.home), tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor)
            )
        },
        bottomBar = {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                color = cardColor,
                tonalElevation = 8.dp,
                shadowElevation = 8.dp
            ) {
                Column(
                    modifier = Modifier
                        .navigationBarsPadding()
                        .padding(horizontal = 16.dp, vertical = 12.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
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
                            Text(stringResource(R.string.previous), fontWeight = FontWeight.Bold)
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
                                containerColor = if (currentFieldIndex == (detectedFields.size - 1).coerceAtLeast(0)) Color(0xFF4CAF50) else Color(0xFF2196F3)
                            )
                        ) {
                            Text(if (currentFieldIndex == (detectedFields.size - 1).coerceAtLeast(0)) stringResource(R.string.finish) else stringResource(R.string.next), fontWeight = FontWeight.Bold)
                            Spacer(Modifier.width(4.dp))
                            Icon(if (currentFieldIndex == (detectedFields.size - 1).coerceAtLeast(0)) Icons.Default.Check else Icons.AutoMirrored.Filled.ArrowForward, null)
                        }
                    }

                    // Repeat Voice Instruction
                    Button(
                        onClick = { 
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                                if (detectedFields.isNotEmpty()) {
                                    viewModel.speakFieldInstruction(detectedFields[currentFieldIndex], selectedLanguage?.code ?: "en")
                                } else if (viewModel.backendGuidance.isNotEmpty()) {
                                    viewModel.speak(viewModel.backendGuidance, selectedLanguage?.code ?: "en")
                                }
                            } else {
                                audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                            }
                        },
                        modifier = Modifier.fillMaxWidth().height(48.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF7986CB)),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.AutoMirrored.Filled.VolumeUp, null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            stringResource(R.string.listen_again),
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Animated field transition
            AnimatedContent(
                targetState = currentFieldIndex,
                transitionSpec = {
                    if (targetState > initialState) {
                        (slideInHorizontally { width -> width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> -width } + fadeOut()
                        )
                    } else {
                        (slideInHorizontally { width -> -width } + fadeIn()).togetherWith(
                            slideOutHorizontally { width -> width } + fadeOut()
                        )
                    }.using(
                        SizeTransform(clip = false)
                    )
                },
                label = "fieldCardTransition"
            ) { targetIndex ->
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(48.dp),
                            shape = CircleShape,
                            color = if (isHighContrast) Color.Yellow else Color(0xFFE8EAF6)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Text(
                                    text = "${targetIndex + 1}",
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isHighContrast) Color.Black else Color(0xFF3F51B5),
                                    fontSize = 20.sp
                                )
                            }
                        }
                        Spacer(Modifier.width(16.dp))
                        Column {
                            Text(
                                text = stringResource(R.string.current_field),
                                fontSize = 12.sp,
                                color = if (isDark) Color.LightGray else Color.Gray,
                                fontWeight = FontWeight.Medium
                            )
                            if (detectedFields.isNotEmpty() && targetIndex in detectedFields.indices) {
                                Text(
                                    text = detectedFields[targetIndex].name,
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isHighContrast) Color.Yellow else textColor
                                )
                            }
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
 
                                // Draw the box for the CURRENT field with float precision
                                val rectWidth = field.bounds.width().toFloat() * scale
                                val rectHeight = field.bounds.height().toFloat() * scale
                                val rectLeft = field.bounds.left.toFloat() * scale + offsetX
                                val rectTop = field.bounds.top.toFloat() * scale + offsetY
 
                                // Glowing semi-transparent background fill (pulsing opacity)
                                drawRoundRect(
                                    color = Color.Red.copy(alpha = 0.12f * boxAlpha),
                                    topLeft = Offset(rectLeft, rectTop),
                                    size = Size(rectWidth, rectHeight),
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx(), 6.dp.toPx())
                                )

                                // Glowing razor-sharp borders (pulsing opacity)
                                drawRoundRect(
                                    color = Color.Red.copy(alpha = boxAlpha),
                                    topLeft = Offset(rectLeft, rectTop),
                                    size = Size(rectWidth, rectHeight),
                                    cornerRadius = androidx.compose.ui.geometry.CornerRadius(6.dp.toPx(), 6.dp.toPx()),
                                    style = Stroke(width = 3.dp.toPx())
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
                Text(stringResource(R.string.ai_analyzing), fontSize = 12.sp, color = textColor)
            } else {
                AnimatedVisibility(
                    visible = viewModel.backendGuidance.isNotEmpty(),
                    enter = slideInVertically { it } + fadeIn(),
                    exit = slideOutVertically { it } + fadeOut()
                ) {
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isDark) Color(0xFF252525) else Color(0xFFF3F5FF)
                        ),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFF3F51B5).copy(alpha = 0.15f))
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(IntrinsicSize.Max)
                        ) {
                            // Left accent colored stripe
                            Box(
                                modifier = Modifier
                                    .width(6.dp)
                                    .fillMaxHeight()
                                    .background(if (isHighContrast) Color.Yellow else Color(0xFF3F51B5))
                            )
                            Column(
                                modifier = Modifier.padding(16.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Stars,
                                        contentDescription = null,
                                        tint = if (isHighContrast) Color.Yellow else Color(0xFFFFC107),
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(Modifier.width(8.dp))
                                    Text(
                                        text = stringResource(R.string.ai_guidance),
                                        fontWeight = FontWeight.ExtraBold,
                                        color = if (isHighContrast) Color.Yellow else Color(0xFF1A237E),
                                        fontSize = 16.sp
                                    )
                                }
                                Spacer(Modifier.height(8.dp))
                                Text(
                                    text = viewModel.backendGuidance,
                                    fontSize = 14.sp,
                                    color = if (isDark) Color.White.copy(alpha = 0.9f) else Color(0xFF2C3E50),
                                    lineHeight = 20.sp,
                                    fontWeight = FontWeight.Normal
                                )
                            }
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
