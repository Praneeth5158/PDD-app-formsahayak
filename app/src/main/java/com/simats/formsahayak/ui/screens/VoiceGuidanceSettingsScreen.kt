package com.simats.formsahayak.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.simats.formsahayak.ui.viewmodel.FormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VoiceGuidanceDetailScreen(
    speed: String,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    viewModel: FormViewModel,
    onBackClick: () -> Unit
) {
    val themeColor = when (speed) {
        "Slow" -> Color(0xFFE67E22) // Orange
        "Fast" -> Color(0xFF2E7D32) // Green
        else -> Color(0xFF1A73E8)   // Blue
    }

    val context = LocalContext.current
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            val sampleText = "Welcome to Form Sahayak. Let me help you fill this form step by step. Please follow the instructions on screen."
            viewModel.speak(sampleText, "en")
        } else {
            Toast.makeText(context, "Audio permission is required to play the sample", Toast.LENGTH_SHORT).show()
        }
    }

    val bgColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF121212) else Color(0xFFF8FBFF)
    val cardBgColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkMode || isHighContrast) Color.White else Color(0xFF2C3E50)
    val secondaryTextColor = if (isDarkMode || isHighContrast) Color.LightGray else Color.Gray

    val statusBg = if (isHighContrast) Color.Black else themeColor.copy(alpha = 0.1f)
    val statusBorder = if (isHighContrast) Color.White else themeColor.copy(alpha = 0.2f)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Voice Guidance Settings", fontSize = 18.sp, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = bgColor,
                    titleContentColor = textColor,
                    navigationIconContentColor = textColor
                )
            )
        },
        containerColor = bgColor
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Status Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = statusBg,
                border = BorderStroke(1.dp, statusBorder)
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .background(themeColor.copy(alpha = 0.2f), CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (speed) {
                                "Slow" -> Icons.Default.SlowMotionVideo
                                "Fast" -> Icons.Default.Bolt
                                else -> Icons.Default.Speed
                            },
                            contentDescription = null,
                            tint = themeColor
                        )
                    }
                    Spacer(modifier = Modifier.width(16.dp))
                    Column {
                        Text(
                            text = "$speed Speed Selected",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = textColor
                        )
                        Text(
                            text = when (speed) {
                                "Slow" -> "Voice will speak slowly for better understanding"
                                "Fast" -> "Voice will speak quickly for efficient navigation"
                                else -> "Standard speaking speed for most users"
                            },
                            fontSize = 12.sp,
                            color = secondaryTextColor
                        )
                    }
                }
            }

            // Voice Speed Info
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = cardBgColor,
                shadowElevation = if (isHighContrast) 0.dp else 2.dp,
                border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Default.VolumeUp, contentDescription = null, tint = themeColor)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Voice Speed: $speed", fontWeight = FontWeight.Bold, color = textColor)
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isDarkMode || isHighContrast) Color.Black else themeColor.copy(alpha = 0.05f),
                        border = BorderStroke(1.dp, themeColor.copy(alpha = 0.2f))
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = themeColor, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Perfect For:", fontWeight = FontWeight.Bold, fontSize = 14.sp, color = textColor)
                            }
                            val points = when (speed) {
                                "Slow" -> listOf("First-time users", "Learning new features", "Complex form instructions", "Better comprehension")
                                "Fast" -> listOf("Experienced users", "Quick form completion", "Familiar with the app", "Time-efficient navigation")
                                else -> listOf("Regular app usage", "Comfortable listening", "Balanced clarity and speed", "Most users prefer this")
                            }
                            points.forEach { point ->
                                Text("• $point", fontSize = 13.sp, color = secondaryTextColor, modifier = Modifier.padding(start = 24.dp, top = 4.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    
                    Text("Speech Rate", fontWeight = FontWeight.SemiBold, fontSize = 14.sp, color = textColor)
                    Spacer(modifier = Modifier.height(8.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Speed Level", fontSize = 12.sp, color = secondaryTextColor, modifier = Modifier.weight(1f))
                        Text(
                            text = when(speed) { "Slow" -> "0.75x"; "Fast" -> "1.25x"; else -> "1.0x" },
                            fontWeight = FontWeight.Bold,
                            color = themeColor,
                            fontSize = 14.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(4.dp))
                    LinearProgressIndicator(
                        progress = { when(speed) { "Slow" -> 0.3f; "Fast" -> 0.8f; else -> 0.5f } },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp),
                        color = themeColor,
                        trackColor = themeColor.copy(alpha = 0.2f),
                        strokeCap = StrokeCap.Round
                    )
                }
            }

            // Try Voice Sample
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = cardBgColor,
                shadowElevation = if (isHighContrast) 0.dp else 2.dp,
                border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text("Try Voice Sample", fontWeight = FontWeight.Bold, color = textColor)
                        Button(
                            onClick = { 
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                                    val sampleText = "Welcome to Form Sahayak. Let me help you fill this form step by step. Please follow the instructions on screen."
                                    viewModel.speak(sampleText, "en")
                                } else {
                                    audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = themeColor),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                            modifier = Modifier.height(32.dp)
                        ) {
                            Icon(Icons.Default.PlayArrow, contentDescription = null, modifier = Modifier.size(16.dp))
                            Spacer(modifier = Modifier.width(4.dp))
                            Text("Play", fontSize = 12.sp)
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(8.dp),
                        color = if (isDarkMode || isHighContrast) Color.Black else Color(0xFFF1F4F9),
                        border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
                    ) {
                        Text(
                            text = "\"Welcome to Form Sahayak. Let me help you fill this form step by step. Please follow the instructions on screen.\"",
                            fontSize = 13.sp,
                            fontStyle = androidx.compose.ui.text.font.FontStyle.Italic,
                            color = secondaryTextColor,
                            modifier = Modifier.padding(12.dp)
                        )
                    }
                }
            }

            // Voice Settings
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = cardBgColor,
                shadowElevation = if (isHighContrast) 0.dp else 2.dp,
                border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text("Voice Settings", fontWeight = FontWeight.Bold, color = textColor)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    var enableVoice by remember { mutableStateOf(true) }
                    var autoPlay by remember { mutableStateOf(true) }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Enable Voice Guidance", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textColor)
                            Text("Turn on/off voice instructions", fontSize = 11.sp, color = secondaryTextColor)
                        }
                        Switch(
                            checked = enableVoice,
                            onCheckedChange = { enableVoice = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = themeColor)
                        )
                    }
                    
                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp), color = textColor.copy(alpha = 0.1f))
                    
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text("Auto-play Instructions", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textColor)
                            Text("Automatically read field instructions", fontSize = 11.sp, color = secondaryTextColor)
                        }
                        Switch(
                            checked = autoPlay,
                            onCheckedChange = { autoPlay = it },
                            colors = SwitchDefaults.colors(checkedTrackColor = themeColor)
                        )
                    }
                }
            }

            // Bottom Recommendation
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = if (isHighContrast) Color.Black else themeColor.copy(alpha = 0.05f),
                border = BorderStroke(1.dp, themeColor.copy(alpha = 0.2f))
            ) {
                Text(
                    text = when(speed) {
                        "Slow" -> "Tip: Slow speed is recommended for elderly users and first-time learners"
                        "Fast" -> "For Advanced Users: Fast speed is ideal when you're familiar with the app interface"
                        else -> "Recommended: Normal speed provides a good balance between clarity and efficiency"
                    },
                    fontSize = 12.sp,
                    color = themeColor,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier.padding(12.dp)
                )
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
