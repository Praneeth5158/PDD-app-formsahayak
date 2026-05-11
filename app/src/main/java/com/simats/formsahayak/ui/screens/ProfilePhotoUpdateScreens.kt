package com.simats.formsahayak.ui.screens

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.graphics.Paint
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UpdateProfilePhotoPopup(
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onDismiss: () -> Unit,
    onTakePhoto: () -> Unit,
    onUploadPhoto: () -> Unit
) {
    val isDark = isDarkMode || isHighContrast
    val backgroundColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryTextColor = if (isDark) Color.LightGray else Color.Gray

    BasicAlertDialog(
        onDismissRequest = onDismiss,
        modifier = Modifier.padding(24.dp)
    ) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = backgroundColor,
            modifier = Modifier.fillMaxWidth(),
            border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        "Update Profile Photo",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = textColor
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = textColor)
                    }
                }

                Text(
                    "Choose how you'd like to add your profile photo",
                    fontSize = 14.sp,
                    color = secondaryTextColor,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                PhotoOptionItem(
                    icon = Icons.Default.CameraAlt,
                    title = "Take Photo",
                    desc = "Use your camera to capture a new photo",
                    iconColor = Color(0xFF2196F3),
                    isDark = isDark,
                    onClick = onTakePhoto
                )

                Spacer(modifier = Modifier.height(16.dp))

                PhotoOptionItem(
                    icon = Icons.Default.Photo,
                    title = "Upload Photo",
                    desc = "Choose a photo from your gallery",
                    iconColor = Color(0xFF4CAF50),
                    isDark = isDark,
                    onClick = onUploadPhoto
                )

                Spacer(modifier = Modifier.height(24.dp))

                Surface(
                    color = if (isDark) Color(0xFF121212) else Color(0xFFF5F5F5),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        "For best results, use a clear photo of your face with good lighting",
                        fontSize = 12.sp,
                        color = secondaryTextColor,
                        textAlign = TextAlign.Center,
                        modifier = Modifier.padding(12.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun PhotoOptionItem(
    icon: ImageVector,
    title: String,
    desc: String,
    iconColor: Color,
    isDark: Boolean,
    onClick: () -> Unit
) {
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryTextColor = if (isDark) Color.LightGray else Color.Gray
    val borderColor = if (isDark) Color.DarkGray else Color(0xFFEEEEEE)

    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(16.dp),
        border = androidx.compose.foundation.BorderStroke(1.dp, borderColor),
        color = if (isDark) Color(0xFF252525) else Color.White,
        modifier = Modifier.fillMaxWidth()
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(40.dp),
                shape = RoundedCornerShape(8.dp),
                color = iconColor.copy(alpha = 0.1f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = iconColor)
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = textColor)
                Text(desc, fontSize = 12.sp, color = secondaryTextColor)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CropPhotoScreen(
    pickedBitmap: Bitmap?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onCancel: () -> Unit,
    onSave: (Bitmap) -> Unit
) {
    val isDark = isDarkMode || isHighContrast
    var zoomLevel by remember { mutableFloatStateOf(1f) }
    var rotationAngle by remember { mutableFloatStateOf(0f) }
    
    val backgroundColor = if (isDark) Color.Black else Color(0xFFF0F7FF)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Adjust Photo", fontWeight = FontWeight.Bold, fontSize = 20.sp) },
                navigationIcon = {
                    IconButton(onClick = onCancel) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = backgroundColor,
                    titleContentColor = textColor,
                    navigationIconContentColor = textColor
                )
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Square Container for the Photo
            Box(
                modifier = Modifier
                    .size(300.dp)
                    .clip(RoundedCornerShape(12.dp)) // Strict clipping to the square box
                    .background(if (isDark) Color.Black else Color.White)
                    .border(2.dp, if (isHighContrast) Color.White else Color.LightGray, RoundedCornerShape(12.dp))
                    .clipToBounds(), // Critical: Prevents contents from overflowing when zooming
                contentAlignment = Alignment.Center
            ) {
                if (pickedBitmap != null) {
                    Image(
                        bitmap = pickedBitmap.asImageBitmap(),
                        contentDescription = "Selected Photo",
                        modifier = Modifier
                            .fillMaxSize()
                            .graphicsLayer(
                                scaleX = zoomLevel,
                                scaleY = zoomLevel,
                                rotationZ = rotationAngle
                            ),
                        contentScale = ContentScale.Fit
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = null,
                        tint = if (isDark) Color.White.copy(alpha = 0.3f) else Color.Gray.copy(alpha = 0.3f),
                        modifier = Modifier.size(120.dp)
                    )
                }

                // Decorative Circular Overlay
                Box(
                    modifier = Modifier
                        .size(240.dp)
                        .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                "Adjust photo within the box boundaries.\nZooming out keeps it inside the frame.",
                color = if (isDark) Color.White else Color.Gray,
                fontSize = 14.sp,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))

            // Zoom Control
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cardColor, RoundedCornerShape(12.dp))
                    .padding(16.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.ZoomIn, contentDescription = null, tint = textColor, modifier = Modifier.size(20.dp))
                    Text("Zoom", color = textColor, modifier = Modifier.padding(start = 8.dp), fontSize = 14.sp)
                    Spacer(modifier = Modifier.weight(1f))
                    Text("${(zoomLevel * 100).toInt()}%", color = textColor, fontSize = 14.sp)
                    Icon(imageVector = Icons.Default.ZoomOut, contentDescription = null, tint = textColor, modifier = Modifier.size(20.dp).padding(start = 8.dp))
                }
                Slider(
                    value = zoomLevel,
                    onValueChange = { zoomLevel = it },
                    valueRange = 0.5f..3.0f,
                    colors = SliderDefaults.colors(
                        thumbColor = if (isDark) Color.White else Color(0xFF2196F3),
                        activeTrackColor = if (isDark) Color.White else Color(0xFF2196F3),
                        inactiveTrackColor = Color.Gray
                    )
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Rotate Control
            Button(
                onClick = { rotationAngle = (rotationAngle + 90f) % 360f },
                modifier = Modifier.fillMaxWidth().height(50.dp),
                colors = ButtonDefaults.buttonColors(containerColor = cardColor),
                shape = RoundedCornerShape(12.dp),
                border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) Color.White else Color(0xFFEEEEEE)),
                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp)
            ) {
                Icon(imageVector = Icons.Default.RotateRight, contentDescription = null, tint = textColor)
                Spacer(modifier = Modifier.width(8.dp))
                Text("Rotate 90°", color = textColor)
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action Buttons
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                OutlinedButton(
                    onClick = onCancel,
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red),
                    border = androidx.compose.foundation.BorderStroke(1.dp, Color.Red)
                ) {
                    Text("Cancel", color = Color.Red)
                }

                Button(
                    onClick = { 
                        if (pickedBitmap != null) {
                            val finalBitmap = applyAdjustments(pickedBitmap, rotationAngle, zoomLevel)
                            onSave(finalBitmap)
                        }
                    },
                    modifier = Modifier.weight(1f).height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
                ) {
                    Text("Save Photo")
                }
            }
        }
    }
}

private fun applyAdjustments(bitmap: Bitmap, angle: Float, zoom: Float): Bitmap {
    val size = Math.max(bitmap.width, bitmap.height)
    val output = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(output)
    
    val drawMatrix = Matrix()
    drawMatrix.postTranslate(-bitmap.width / 2f, -bitmap.height / 2f)
    drawMatrix.postRotate(angle)
    drawMatrix.postScale(zoom, zoom)
    drawMatrix.postTranslate(size / 2f, size / 2f)
    
    canvas.drawBitmap(bitmap, drawMatrix, Paint(Paint.FILTER_BITMAP_FLAG))
    return output
}

@Composable
fun ProfilePhotoUpdatedScreen(
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    userName: String,
    onGoToProfile: () -> Unit
) {
    val isDark = isDarkMode || isHighContrast
    val backgroundColor = if (isDark) Color.Black else Color(0xFFF0F7FF)
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryTextColor = if (isDark) Color.LightGray else Color.Gray
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Surface(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            color = if (isDark) Color(0xFF1B5E20) else Color(0xFFE8F5E9)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = Color(0xFF4CAF50),
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            "Profile Photo\nUpdated!",
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = textColor,
            textAlign = TextAlign.Center,
            lineHeight = 40.sp
        )

        Text(
            "Your profile picture has been\nsuccessfully updated",
            fontSize = 16.sp,
            color = secondaryTextColor,
            textAlign = TextAlign.Center,
            modifier = Modifier.padding(top = 16.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        // Mini Profile Card
        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(16.dp),
            color = cardColor,
            shadowElevation = if (isHighContrast) 0.dp else 2.dp,
            border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFEEEEEE))
        ) {
            Row(
                modifier = Modifier.padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Surface(
                    modifier = Modifier.size(60.dp),
                    shape = CircleShape,
                    color = Color(0xFF6366F1)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color.White, modifier = Modifier.size(32.dp))
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column {
                    Text(userName, fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor)
                    Text("Premium Member", color = secondaryTextColor, fontSize = 12.sp)
                    Text("Photo updated successfully", color = Color(0xFF4CAF50), fontSize = 12.sp, fontWeight = FontWeight.Medium)
                }
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(12.dp),
            color = if (isDark) Color(0xFF0D47A1) else Color.White
        ) {
            Text(
                "Your new profile photo is now visible across the app",
                fontSize = 12.sp,
                color = if (isDark) Color.White else Color(0xFF1976D2),
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(16.dp)
            )
        }

        Spacer(modifier = Modifier.height(48.dp))

        Button(
            onClick = onGoToProfile,
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
        ) {
            Text("Go to Profile", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
        }
    }
}
