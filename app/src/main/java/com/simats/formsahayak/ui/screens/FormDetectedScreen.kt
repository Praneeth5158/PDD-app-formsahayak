package com.simats.formsahayak.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.simats.formsahayak.R
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
    val context = LocalContext.current
    val audioPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.speak(formType, selectedLanguage?.code ?: "en")
        } else {
            Toast.makeText(context, context.getString(R.string.audio_denied), Toast.LENGTH_SHORT).show()
        }
    }

    val backgroundColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF121212) else Color(0xFFF8FBFF)
    val cardColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDarkMode || isHighContrast) Color.White else Color(0xFF1A237E)
    val secondaryTextColor = if (isDarkMode || isHighContrast) Color.LightGray else Color(0xFF64748B)
    val accentColor = if (isHighContrast) Color.Yellow else if (isDarkMode) Color(0xFF63B3ED) else Color(0xFF1E40AF)
    val cardBorderColor = if (isHighContrast) Color.White else if (isDarkMode) Color(0xFF333333) else Color(0xFFE2E8F0)

    val statusText = when {
        confidence >= 90 -> stringResource(R.string.fd_excellent)
        confidence >= 75 -> stringResource(R.string.fd_ready)
        confidence >= 50 -> stringResource(R.string.fd_fair)
        else -> stringResource(R.string.fd_unclear)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back), tint = textColor)
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
                    text = if (viewModel.scannedPages.size > 1) stringResource(R.string.page_detected_title, viewModel.currentGuidingPageIndex + 1) else stringResource(R.string.form_detected_title),
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = stringResource(R.string.fd_subtitle),
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
                            stringResource(R.string.fd_detected_label),
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
                            onClick = {
                                if (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) {
                                    viewModel.speak(formType, selectedLanguage?.code ?: "en")
                                } else {
                                    audioPermissionLauncher.launch(Manifest.permission.RECORD_AUDIO)
                                }
                            },
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
                                    Text(stringResource(R.string.fd_listen_label), fontWeight = FontWeight.SemiBold)
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
                                stringResource(R.string.fd_proceed_q),
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = accentColor
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                stringResource(R.string.fd_proceed_desc),
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
                                Text(stringResource(R.string.fd_bank_type), fontSize = 12.sp, color = secondaryTextColor)
                                Text(accountType, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(stringResource(R.string.fd_doc_pages), fontSize = 12.sp, color = secondaryTextColor)
                                Text("${viewModel.scannedPages.size} ${stringResource(R.string.fd_pages_suffix)}", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
                            }
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                        Row(modifier = Modifier.fillMaxWidth()) {
                            Column(modifier = Modifier.weight(1f)) {
                                Text(stringResource(R.string.fd_confidence), fontSize = 12.sp, color = secondaryTextColor)
                                Text("$confidence%", fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color(0xFF10B981))
                            }
                            Column(modifier = Modifier.weight(1f)) {
                                Text(stringResource(R.string.fd_status), fontSize = 12.sp, color = secondaryTextColor)
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
                                stringResource(R.string.fd_yes_proceed),
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
                        stringResource(R.string.fd_no_rescan),
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
                        stringResource(R.string.go_back),
                        color = secondaryTextColor,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }
    }
}
