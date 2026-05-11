package com.simats.formsahayak.ui.screens

import android.net.Uri
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
import androidx.compose.material.icons.outlined.Image
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.PhotoCamera
import androidx.compose.material.icons.outlined.WbSunny
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import com.simats.formsahayak.ui.viewmodel.FormViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UploadFormScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onBackClick: () -> Unit,
    onImageSelected: (Uri) -> Unit
) {
    val context = LocalContext.current
    val isDark = isDarkMode || isHighContrast
    val backgroundColor = if (isDark) Color.Black else Color(0xFFF8FBFF)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    // Translation logic
    val title = when (selectedLanguage?.code) {
        "te" -> "ఫారమ్‌ను అప్‌లోడ్ చేయండి"
        "ta" -> "படிவத்தைப் பதிவேற்றவும்"
        else -> "Upload Form"
    }
    val subtitle = when (selectedLanguage?.code) {
        "te" -> "స్పష్టమైన ఫోటో తీయండి లేదా గ్యాలరీ నుండి అప్‌లోడ్ చేయండి"
        "ta" -> "தெளிவான புகைப்படத்தை எடுக்கவும் அல்லது கேலரியில் இருந்து பதிவேற்றவும்"
        else -> "Take a clear photo or upload\nfrom gallery"
    }
    val cameraText = when (selectedLanguage?.code) {
        "te" -> "కెమెరా"
        "ta" -> "கேமரா"
        else -> "Camera"
    }
    val galleryText = when (selectedLanguage?.code) {
        "te" -> "గ్యాలరీ"
        "ta" -> "கேலரி"
        else -> "Gallery"
    }
    val tipsTitle = when (selectedLanguage?.code) {
        "te" -> "ఉత్తమ ఫలితాల కోసం చిట్కాలు"
        "ta" -> "சிறந்த முடிவுகளுக்கான உதவிக்குறிப்புகள்"
        else -> "Tips for best results"
    }

    val galleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let { onImageSelected(it) }
    }

    val tempUri = remember {
        val file = File(context.cacheDir, "temp_capture.jpg")
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    val cameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) onImageSelected(tempUri)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = textColor) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = if (isDark) Color.Black else Color.White)
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(28.dp),
                color = cardColor,
                shadowElevation = if (isHighContrast) 0.dp else 4.dp,
                border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
            ) {
                Column(modifier = Modifier.padding(24.dp), horizontalAlignment = Alignment.CenterHorizontally) {
                    Surface(modifier = Modifier.size(100.dp), shape = CircleShape, color = Color(0xFFE3F2FD)) {
                        Box(contentAlignment = Alignment.Center) {
                            Icon(imageVector = Icons.Outlined.Image, contentDescription = null, tint = Color(0xFF2196F3), modifier = Modifier.size(50.dp))
                        }
                    }
                    Spacer(modifier = Modifier.height(24.dp))
                    Text(text = title, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = textColor)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = subtitle, textAlign = TextAlign.Center, fontSize = 14.sp, color = if (isDark) Color.LightGray else Color.Gray)
                    Spacer(modifier = Modifier.height(32.dp))
                    Button(
                        onClick = { cameraLauncher.launch(tempUri) },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                    ) {
                        Icon(imageVector = Icons.Default.PhotoCamera, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(cameraText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    OutlinedButton(
                        onClick = { galleryLauncher.launch("image/*") },
                        modifier = Modifier.fillMaxWidth().height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(2.dp, if (isHighContrast) Color.White else Color(0xFF4CAF50)),
                        colors = ButtonDefaults.outlinedButtonColors(contentColor = if (isHighContrast) Color.White else Color(0xFF4CAF50))
                    ) {
                        Icon(imageVector = Icons.Default.FileUpload, contentDescription = null)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(galleryText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            val tipGradient = Brush.verticalGradient(colors = listOf(Color(0xFFFF9800), Color(0xFFFFC107)))
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = Color.Transparent,
                border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
            ) {
                val bannerBrush = if (isHighContrast) Brush.linearGradient(listOf(Color.Black, Color.Black)) else tipGradient
                Column(modifier = Modifier.background(bannerBrush).padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(modifier = Modifier.size(36.dp), shape = CircleShape, color = Color.White.copy(alpha = 0.2f)) {
                            Box(contentAlignment = Alignment.Center) { Icon(imageVector = Icons.Outlined.Lightbulb, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp)) }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(text = tipsTitle, color = Color.White, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    TipItem(icon = Icons.Outlined.WbSunny, text = "Ensure good lighting")
                    Spacer(modifier = Modifier.height(12.dp))
                    TipItem(icon = Icons.Outlined.Image, text = "Keep form flat and clear")
                    Spacer(modifier = Modifier.height(12.dp))
                    TipItem(icon = Icons.Outlined.PhotoCamera, text = "Avoid shadows and glare")
                }
            }
        }
    }
}

@Composable
fun TipItem(icon: ImageVector, text: String) {
    Surface(modifier = Modifier.fillMaxWidth(), shape = RoundedCornerShape(12.dp), color = Color.White.copy(alpha = 0.15f)) {
        Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
            Icon(imageVector = icon, contentDescription = null, tint = Color.White, modifier = Modifier.size(18.dp))
            Spacer(modifier = Modifier.width(12.dp))
            Text(text = text, color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.Medium)
        }
    }
}
