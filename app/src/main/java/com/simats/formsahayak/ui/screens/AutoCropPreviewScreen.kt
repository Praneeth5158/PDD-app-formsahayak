package com.simats.formsahayak.ui.screens

import android.graphics.Bitmap
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AutoCropPreviewScreen(
    imageBitmap: Bitmap?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onCancel: () -> Unit,
    onAccept: (Bitmap?) -> Unit
) {
    val backgroundColor = Color(0xFF0B0F1A)
    val textColor = Color.White
    var isDetecting by remember { mutableStateOf(true) }

    LaunchedEffect(Unit) {
        delay(400) // Fast detection simulation
        isDetecting = false
    }

    // Corner positions for the crop area
    var topLeft by remember { mutableStateOf(Offset(200f, 250f)) }
    var topRight by remember { mutableStateOf(Offset(800f, 250f)) }
    var bottomLeft by remember { mutableStateOf(Offset(200f, 1250f)) }
    var bottomRight by remember { mutableStateOf(Offset(800f, 1250f)) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Auto Crop Preview", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = textColor)
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
                .padding(bottom = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Image Preview Area
            Box(
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .background(Color(0xFF161B22), RoundedCornerShape(12.dp)),
                contentAlignment = Alignment.Center
            ) {
                if (imageBitmap != null) {
                    Box(modifier = Modifier.padding(24.dp)) {
                        Image(
                            bitmap = imageBitmap.asImageBitmap(),
                            contentDescription = "Form Preview",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                            alpha = 0.7f
                        )
                        
                        if (!isDetecting) {
                            Canvas(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .pointerInput(Unit) {
                                        detectDragGestures { change, dragAmount ->
                                            change.consume()
                                            val touch = change.position
                                            // Proximity check for corners to allow dragging
                                            when {
                                                (touch - topLeft).getDistance() < 120f -> topLeft += dragAmount
                                                (touch - topRight).getDistance() < 120f -> topRight += dragAmount
                                                (touch - bottomLeft).getDistance() < 120f -> bottomLeft += dragAmount
                                                (touch - bottomRight).getDistance() < 120f -> bottomRight += dragAmount
                                            }
                                        }
                                    }
                            ) {
                                // Draw the dynamic crop boundary
                                val path = androidx.compose.ui.graphics.Path().apply {
                                    moveTo(topLeft.x, topLeft.y)
                                    lineTo(topRight.x, topRight.y)
                                    lineTo(bottomRight.x, bottomRight.y)
                                    lineTo(bottomLeft.x, bottomLeft.y)
                                    close()
                                }
                                
                                drawPath(
                                    path = path,
                                    color = Color(0xFF22C55E),
                                    style = Stroke(
                                        width = 3f,
                                        pathEffect = PathEffect.dashPathEffect(floatArrayOf(15f, 10f), 0f)
                                    )
                                )
                                
                                // Interactive Corner Handles
                                val corners = listOf(topLeft, topRight, bottomLeft, bottomRight)
                                corners.forEach { corner ->
                                    drawCircle(Color(0xFF22C55E).copy(alpha = 0.2f), radius = 40f, center = corner)
                                    drawCircle(Color(0xFF22C55E), radius = 12f, center = corner)
                                    drawCircle(Color.White, radius = 6f, center = corner)
                                }
                            }

                            // Auto-detected Badge
                            Surface(
                                modifier = Modifier.padding(12.dp).align(Alignment.TopStart),
                                color = Color(0xFF22C55E),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    "Auto-detected",
                                    color = Color.White,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp),
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                "Adjust corners if needed, or accept auto-crop",
                color = Color.Gray,
                fontSize = 14.sp,
                modifier = Modifier.padding(bottom = 24.dp)
            )

            // Bottom Action Buttons - Side by Side (No Middle Box)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color(0xFFEF4444)),
                    border = BorderStroke(1.dp, Color(0xFFEF4444))
                ) {
                    Icon(Icons.Default.Close, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Cancel", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
                
                Button(
                    onClick = { onAccept(imageBitmap) },
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF22C55E)),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("Accept", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }
        }
    }
}
