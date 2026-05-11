package com.simats.formsahayak.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.formsahayak.ui.viewmodel.FormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EnhanceImageScreen(
    imageBitmap: Bitmap?,
    viewModel: FormViewModel,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onBackClick: () -> Unit,
    onApplyClick: (Bitmap?) -> Unit
) {
    val backgroundColor = Color.White
    val textColor = Color.Black
    val secondaryTextColor = Color.Gray

    var currentBitmap by remember { mutableStateOf(imageBitmap) }
    var brightness by remember { mutableFloatStateOf(1.0f) }
    var contrast by remember { mutableFloatStateOf(1.0f) }
    
    // We update the preview when sliders change
    val displayedBitmap = remember(currentBitmap, brightness, contrast) {
        currentBitmap?.let { viewModel.applyEnhancements(it, brightness, contrast) }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                        Text("Enhance Image", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor)
                    }
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = textColor)
                    }
                },
                actions = {
                    TextButton(onClick = { 
                        brightness = 1.0f
                        contrast = 1.0f
                        currentBitmap = imageBitmap
                    }) {
                        Text("Reset", color = Color(0xFF2563EB), fontWeight = FontWeight.SemiBold)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = Color.White)
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image Preview Area
            Surface(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = Color(0xFF1E293B)
            ) {
                Box(
                    modifier = Modifier.padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (displayedBitmap != null) {
                        Image(
                            bitmap = displayedBitmap.asImageBitmap(),
                            contentDescription = "Preview",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Adjustment Controls
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Brightness
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("☀", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Brightness", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textColor)
                        }
                        Text("${(brightness * 100).toInt()}%", fontSize = 12.sp, color = secondaryTextColor)
                    }
                    Slider(
                        value = brightness,
                        onValueChange = { brightness = it },
                        valueRange = 0.5f..1.5f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color.Black,
                            activeTrackColor = Color.Black,
                            inactiveTrackColor = Color(0xFFE2E8F0)
                        )
                    )
                }

                // Contrast
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text("◑", fontSize = 16.sp)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text("Contrast", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textColor)
                        }
                        Text("${(contrast * 100).toInt()}%", fontSize = 12.sp, color = secondaryTextColor)
                    }
                    Slider(
                        value = contrast,
                        onValueChange = { contrast = it },
                        valueRange = 0.5f..1.5f,
                        colors = SliderDefaults.colors(
                            thumbColor = Color.Black,
                            activeTrackColor = Color.Black,
                            inactiveTrackColor = Color(0xFFE2E8F0)
                        )
                    )
                }

                // Rotation Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Rotation", fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textColor)
                    
                    OutlinedButton(
                        onClick = { 
                            currentBitmap?.let { 
                                currentBitmap = viewModel.rotateBitmap(it) 
                            }
                        },
                        shape = RoundedCornerShape(8.dp),
                        border = BorderStroke(1.dp, Color(0xFFE2E8F0)),
                        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 8.dp)
                    ) {
                        Icon(Icons.Default.Refresh, contentDescription = null, modifier = Modifier.size(16.dp), tint = textColor)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("Rotate 90°", color = textColor, fontSize = 12.sp)
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Apply Button
            Button(
                onClick = { onApplyClick(displayedBitmap) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp)
                    .padding(bottom = 8.dp),
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E))
            ) {
                Icon(Icons.Default.Check, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Apply & Continue", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
            
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
