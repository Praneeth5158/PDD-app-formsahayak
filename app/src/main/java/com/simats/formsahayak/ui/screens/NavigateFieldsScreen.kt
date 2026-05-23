package com.simats.formsahayak.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.simats.formsahayak.ui.viewmodel.FormViewModel

@Composable
fun NavigateFieldsScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    viewModel: FormViewModel,
    onBackClick: () -> Unit,
    onGuideClick: (com.simats.formsahayak.logic.DetectedField) -> Unit
) {
    val isDark = isDarkMode || isHighContrast
    val context = LocalContext.current
    val backgroundColor = if (isDark) Color.Black else Color(0xFFF8FBFF)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    val fields = viewModel.detectedFields
    var currentFieldIndex by remember { mutableStateOf(0) }

    val audioPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.speakFieldInstruction(fields[currentFieldIndex], selectedLanguage?.code ?: "en")
            onGuideClick(fields[currentFieldIndex])
        } else {
            val deniedMsg = when (selectedLanguage?.code) {
                "te" -> "వాయిస్ గైడెన్స్ కోసం ఆడియో అనుమతి అవసరం"
                "ta" -> "గురల్ వళిగాట్టుదలుక్కు ఆడియో అనుమతి తేవై"
                "hi" -> "आवाज मार्गदर्शन के लिए ऑडियो अनुमति की आवश्यकता है"
                else -> "Audio permission is required for voice guidance"
            }
            Toast.makeText(context, deniedMsg, Toast.LENGTH_SHORT).show()
            onGuideClick(fields[currentFieldIndex])
        }
    }
    
    if (fields.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No fields detected", color = textColor)
        }
        return
    }

    val progress = (currentFieldIndex + 1).toFloat() / fields.size

    // Translation logic
    val title = when (selectedLanguage?.code) {
        "te" -> "ఫీల్డ్లను నావిగేట్ చేయండి"
        "ta" -> "புலங்களை வழிநடத்துங்கள்"
        "hi" -> "फ़ील्ड नेविगेट करें"
        else -> "Navigate Fields"
    }
    val guideButtonText = when (selectedLanguage?.code) {
        "te" -> "మార్గదర్శకం"
        "ta" -> "வழிகாட்டி"
        "hi" -> "गाइड"
        else -> "Guide"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = title,
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            modifier = Modifier.padding(vertical = 16.dp)
        )

        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            shape = RoundedCornerShape(32.dp),
            color = cardColor,
            shadowElevation = if (isHighContrast) 0.dp else 4.dp,
            border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Field ${currentFieldIndex + 1} of ${fields.size}",
                        fontSize = 12.sp,
                        color = if (isDark) Color.LightGray else Color.Gray
                    )
                    Text(
                        text = "${(progress * 100).toInt()}%",
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color(0xFF2196F3)
                    )
                }
                
                Spacer(modifier = Modifier.height(8.dp))
                
                LinearProgressIndicator(
                    progress = { progress },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(8.dp),
                    color = Color(0xFF00ACC1),
                    trackColor = if (isDark) Color(0xFF333333) else Color(0xFFE0E0E0),
                    strokeCap = androidx.compose.ui.graphics.StrokeCap.Round
                )

                Spacer(modifier = Modifier.height(32.dp))

                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = Color(0xFF2196F3)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Text(
                            text = "${currentFieldIndex + 1}",
                            color = Color.White,
                            fontSize = 40.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = fields[currentFieldIndex].name,
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = textColor
                )

                Spacer(modifier = Modifier.height(32.dp))

                fields.forEachIndexed { index, field ->
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp)
                            .padding(vertical = 4.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = if (index == currentFieldIndex) Color(0xFF2196F3) else (if (isDark) Color(0xFF333333) else Color(0xFFF5F5F5)),
                        onClick = { currentFieldIndex = index }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${field.id}. ${field.name}",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (index == currentFieldIndex) Color.White else (if (isDark) Color.White else Color.Black)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { 
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                            viewModel.speakFieldInstruction(fields[currentFieldIndex], selectedLanguage?.code ?: "en")
                            onGuideClick(fields[currentFieldIndex])
                        } else {
                            audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF00C853))
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(guideButtonText, fontSize = 18.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}
