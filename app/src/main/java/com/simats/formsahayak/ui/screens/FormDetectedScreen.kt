package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.formsahayak.ui.viewmodel.FormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDetectedScreen(
    formType: String,
    accountType: String,
    confidence: Int,
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    viewModel: FormViewModel,
    onProceed: () -> Unit,
    onRescan: () -> Unit,
    onBack: () -> Unit
) {
    val backgroundColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF121212) else Color(0xFFF8FBFF)
    val cardColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkMode || isHighContrast) Color.White else Color(0xFF1A237E)
    val secondaryTextColor = if (isDarkMode || isHighContrast) Color.LightGray else Color(0xFF64748B)
    val accentColor = if (isHighContrast) Color.Yellow else if (isDarkMode) Color(0xFF63B3ED) else Color(0xFF1E40AF)
    val cardBorderColor = if (isHighContrast) Color.White else if (isDarkMode) Color(0xFF333333) else Color(0xFFE2E8F0)

    val statusText = when (selectedLanguage?.code) {
        "te" -> when {
            confidence >= 90 -> "అద్భుతం"
            confidence >= 75 -> "సిద్ధంగా ఉంది"
            confidence >= 50 -> "పర్వాలేదు"
            else -> "అస్పష్టంగా ఉంది"
        }
        "ta" -> when {
            confidence >= 90 -> "மிகச்சிறந்தது"
            confidence >= 75 -> "தயார்"
            confidence >= 50 -> "பரவாயில்லை"
            else -> "தெளிவற்றது"
        }
        else -> when {
            confidence >= 90 -> "Excellent"
            confidence >= 75 -> "Ready"
            confidence >= 50 -> "Fair"
            else -> "Unclear"
        }
    }

    val title = when (selectedLanguage?.code) {
        "te" -> if (viewModel.scannedPages.size > 1) "పేజీ ${viewModel.currentGuidingPageIndex + 1} గుర్తించబడింది" else "ఫారమ్ గుర్తించబడింది"
        "ta" -> if (viewModel.scannedPages.size > 1) "பக்கம் ${viewModel.currentGuidingPageIndex + 1} கண்டறியப்பட்டது" else "படிவம் கண்டறியப்பட்டது"
        else -> if (viewModel.scannedPages.size > 1) "Page ${viewModel.currentGuidingPageIndex + 1} Detected" else "Form Detected"
    }
    
    val subtitle = when (selectedLanguage?.code) {
        "te" -> "మీరు అప్‌లోడ్ చేసిన ఫారమ్ రకాన్ని మేము గుర్తించాము"
        "ta" -> "நீங்கள் பதிவேற்றிய படிவத்தின் வகையை நாங்கள் கண்டறிந்தோம்"
        else -> "We identified the type of form you uploaded"
    }

    val detectedLabel = when (selectedLanguage?.code) {
        "te" -> "గుర్తించబడిన ఫారమ్ రకం:"
        "ta" -> "கண்டறியப்பட்ட படிவ வகை:"
        else -> "Detected Form Type:"
    }

    val listenLabel = when (selectedLanguage?.code) {
        "te" -> "ఫారమ్ రకాన్ని వినండి"
        "ta" -> "படிவ வகையைக் கேளுங்கள்"
        else -> "Listen to Form Type"
    }

    val proceedQuestion = when (selectedLanguage?.code) {
        "te" -> "మేము ఈ ఫారమ్‌తో కొనసాగవచ్చా?"
        "ta" -> "நாங்கள் இந்தப் படிவத்தைத் தொடரலாமா?"
        else -> "Can we proceed with this form?"
    }

    val proceedDesc = when (selectedLanguage?.code) {
        "te" -> "ఫారమ్ రకం సరైనదైతే, కొనసాగడానికి 'అవును' క్లిక్ చేయండి. లేకపోతే, 'మళ్లీ స్కాన్ చేయి' క్లిక్ చేయండి."
        "ta" -> "படிவ வகை சரியாக இருந்தால், தொடர 'ஆம்' என்பதைக் கிளிக் செய்யவும். இல்லையெனில், 'மீண்டும் ஸ்கேன் செய்' என்பதைக் கிளிக் செய்யவும்."
        else -> "If the form type is correct, click 'Proceed' to continue. Otherwise, click 'Re-scan Form' to upload again."
    }

    val bankTypeLabel = when (selectedLanguage?.code) {
        "te" -> "బ్యాంక్ రకం"
        "ta" -> "வங்கி வகை"
        else -> "Bank Type"
    }

    val docPagesLabel = when (selectedLanguage?.code) {
        "te" -> "పత్రం పేజీలు"
        "ta" -> "ஆவணப் பக்கங்கள்"
        else -> "Document Pages"
    }

    val confidenceLabel = when (selectedLanguage?.code) {
        "te" -> "ఖచ్చితత్వం"
        "ta" -> "துல்லியம்"
        else -> "Confidence"
    }

    val statusLabel = when (selectedLanguage?.code) {
        "te" -> "స్థితి"
        "ta" -> "நிலை"
        else -> "Status"
    }

    val pagesSuffix = when (selectedLanguage?.code) {
        "te" -> "పేజీలు"
        "ta" -> "பக்கங்கள்"
        else -> "Pages"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = backgroundColor)
            )
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(cardColor, RoundedCornerShape(24.dp))
                    .border(BorderStroke(1.dp, cardBorderColor), RoundedCornerShape(24.dp))
                    .verticalScroll(rememberScrollState())
                    .padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Document Icon
                Surface(
                    modifier = Modifier.size(60.dp),
                    shape = CircleShape,
                    color = if (isHighContrast) Color.Yellow else Color(0xFFDCFCE7)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            Icons.Default.Description,
                            null,
                            tint = if (isHighContrast) Color.Black else Color(0xFF16A34A),
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = subtitle,
                    fontSize = 14.sp,
                    color = secondaryTextColor,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.padding(horizontal = 16.dp)
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Detected Form Type Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF2D3748) else Color(0xFFF0F7FF),
                    border = BorderStroke(1.dp, if (isHighContrast) Color.White else Color(0xFFBFDBFE))
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            detectedLabel,
                            fontSize = 13.sp,
                            color = secondaryTextColor
                        )
                        Text(
                            formType.ifEmpty { "Deposit Form" },
                            fontSize = 26.sp,
                            fontWeight = FontWeight.Bold,
                            color = accentColor,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(12.dp))
                        
                        // Listen Button
                        Button(
                            onClick = { viewModel.speak(formType, selectedLanguage?.code ?: "en") },
                            modifier = Modifier
                                .fillMaxWidth(0.85f)
                                .height(48.dp),
                            contentPadding = PaddingValues(0.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                            shape = RoundedCornerShape(10.dp)
                        ) {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(
                                        Brush.horizontalGradient(
                                            listOf(Color(0xFFB14BF4), Color(0xFFF2468E))
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.AutoMirrored.Filled.VolumeUp, null, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(listenLabel, fontWeight = FontWeight.SemiBold)
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Info Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF2D3748) else Color(0xFFF0F7FF).copy(alpha = 0.5f),
                    border = BorderStroke(1.dp, if (isHighContrast) Color.White else Color(0xFFDBEAFE))
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.Top
                    ) {
                        Icon(
                            Icons.Default.CheckCircle,
                            null,
                            tint = if (isHighContrast) Color.Yellow else Color(0xFF2563EB),
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                proceedQuestion,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                proceedDesc,
                                fontSize = 12.sp,
                                lineHeight = 16.sp,
                                color = if (isHighContrast) Color.White else if (isDarkMode) Color.LightGray else Color(0xFF3B82F6)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Stats Card
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    color = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF2D3748) else Color.White,
                    border = BorderStroke(1.dp, if (isHighContrast) Color.White else Color(0xFFF1F5F9))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(bankTypeLabel, fontSize = 12.sp, color = secondaryTextColor)
                                Text(accountType, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(docPagesLabel, fontSize = 12.sp, color = secondaryTextColor)
                                Text("${viewModel.scannedPages.size} $pagesSuffix", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(confidenceLabel, fontSize = 12.sp, color = secondaryTextColor)
                                Text("$confidence%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(statusLabel, fontSize = 12.sp, color = secondaryTextColor)
                                Text(statusText, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = if (isHighContrast) Color.Yellow else Color(0xFF2563EB))
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Button(
                    onClick = onProceed,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    contentPadding = PaddingValues(0.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(
                                Brush.horizontalGradient(
                                    listOf(Color(0xFF00C853), Color(0xFF2196F3))
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(Icons.Default.CheckCircle, null, modifier = Modifier.size(18.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                when (selectedLanguage?.code) {
                                    "te" -> "అవును, ఈ ఫారమ్‌తో కొనసాగండి"
                                    "ta" -> "ஆம், இந்தப் படிவத்தைத் தொடரவும்"
                                    else -> "Yes, Proceed with this Form"
                                },
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onRescan,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = BorderStroke(1.dp, if (isHighContrast) Color.White else Color(0xFFFED7AA))
                ) {
                    Icon(Icons.Default.Refresh, null, tint = if (isHighContrast || isDarkMode) Color.White else Color.Black, modifier = Modifier.size(18.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        when (selectedLanguage?.code) {
                            "te" -> "లేదు, ఫారమ్‌ను మళ్లీ స్కాన్ చేయండి"
                            "ta" -> "இல்லை, படிவத்தை மீண்டும் ஸகேன் செய்யவும்"
                            else -> "No, Re-scan Form"
                        },
                        color = if (isHighContrast || isDarkMode) Color.White else Color.Black,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                TextButton(
                    onClick = onBack,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        when (selectedLanguage?.code) {
                            "te" -> "తిరిగి వెళ్ళండి"
                            "ta" -> "திரும்பி செல்"
                            else -> "Go Back"
                        },
                        color = secondaryTextColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
