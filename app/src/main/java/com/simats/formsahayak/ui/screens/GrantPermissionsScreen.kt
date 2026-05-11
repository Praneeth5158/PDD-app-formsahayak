package com.simats.formsahayak.ui.screens

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
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
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.Shield
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.LifecycleEventObserver

@Composable
fun GrantPermissionsScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onAllGranted: () -> Unit,
    onBack: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    var cameraGranted by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) }
    var storageGranted by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, storagePermission) == PackageManager.PERMISSION_GRANTED) }
    var micGranted by remember { mutableStateOf(ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED) }

    // Re-check permissions on resume
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            if (event == Lifecycle.Event.ON_RESUME) {
                cameraGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                storageGranted = ContextCompat.checkSelfPermission(context, storagePermission) == PackageManager.PERMISSION_GRANTED
                micGranted = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
            }
        }
        lifecycleOwner.lifecycle.addObserver(observer)
        onDispose { lifecycleOwner.lifecycle.removeObserver(observer) }
    }

    val launcher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestMultiplePermissions()
    ) { result ->
        cameraGranted = result[Manifest.permission.CAMERA] ?: (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED)
        storageGranted = result[storagePermission] ?: (ContextCompat.checkSelfPermission(context, storagePermission) == PackageManager.PERMISSION_GRANTED)
        micGranted = result[Manifest.permission.RECORD_AUDIO] ?: (ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED)
        
        val allNowGranted = cameraGranted && storageGranted && micGranted
        if (allNowGranted) {
            onAllGranted()
        } else {
            val toastMsg = when(selectedLanguage?.code) {
                "te" -> "కొనసాగడానికి అన్ని అనుమతులు అవసరం"
                "ta" -> "தொடர அனைத்து அனுமதிகளும் தேவை"
                else -> "All permissions are required to continue"
            }
            Toast.makeText(context, toastMsg, Toast.LENGTH_SHORT).show()
        }
    }

    val allGranted = cameraGranted && storageGranted && micGranted

    val backgroundColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF121212) else Color(0xFFF8FBFF)
    val textColor = if (isHighContrast) Color.Yellow else if (isDarkMode) Color.White else Color.Black
    val subTextColor = if (isHighContrast) Color.White else if (isDarkMode) Color.LightGray else Color.Gray
    val cardBorder = if (isHighContrast) Color.White else if (isDarkMode) Color(0xFF333333) else Color(0xFFE5E7EB)

    Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(24.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(20.dp))

            Surface(
                modifier = Modifier.size(64.dp),
                shape = CircleShape,
                color = if (isHighContrast) Color.Yellow else if (isDarkMode) Color(0xFF333333) else Color(0xFFE8F0FE)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Outlined.Shield,
                        contentDescription = null,
                        tint = if (isHighContrast) Color.Black else if (isDarkMode) Color.White else Color(0xFF1A73E8),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            Text(
                text = when (selectedLanguage?.code) {
                    "te" -> "అనుమతులు మంజూరు చేయండి"
                    "ta" -> "அனுமதிகளை வழங்கவும்"
                    else -> "Grant Permissions"
                },
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = when (selectedLanguage?.code) {
                    "te" -> "ఫారమ్‌లను పూరించడంలో మీకు సహాయం చేయడానికి FormSahayakకు ఈ అనుమతులు అవసరం"
                    "ta" -> "படிவங்களை நிரப்ப உங்களுக்கு உதவ FormSahayak-க்கு இந்த அனுமதிகள் தேவை"
                    else -> "FormSahayak needs these permissions to help you fill forms"
                },
                fontSize = 14.sp,
                color = subTextColor,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 20.dp)
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Privacy Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF2D3748) else Color(0xFFF1F6FF),
                border = BorderStroke(1.dp, if (isHighContrast) Color.White else if (isDarkMode) Color(0xFF4A5568) else Color(0xFFD1E3FF))
            ) {
                val privacyTitleStr = when (selectedLanguage?.code) { "te" -> "గోప్యత మొదట: "; "ta" -> "தனியுரிமை முதலில்: "; else -> "Privacy First: " }
                val privacyDescStr = when (selectedLanguage?.code) { 
                    "te" -> "మీ డేటా మీ పరికరంలోనే ఉంటుంది. మేము మీకు సహాయం చేయడానికి మాత్రమే ఈ అనుమతులను ఉపయోగిస్తాము."
                    "ta" -> "உங்கள் தரவு உங்கள் சாதனத்திலேயே இருக்கும். உங்களுக்கு உதவ மட்டுமே இந்த அனுமதிகளைப் பயன்படுத்துகிறோம்."
                    else -> "Your data stays on your device. We only use these permissions to help you."
                }
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = if (isHighContrast) Color.Yellow else if (isDarkMode) Color(0xFF63B3ED) else Color(0xFF1967D2))) {
                            append(privacyTitleStr)
                        }
                        append(privacyDescStr)
                    },
                    fontSize = 13.sp,
                    color = if (isHighContrast) Color.White else if (isDarkMode) Color.LightGray else Color(0xFF1967D2),
                    modifier = Modifier.padding(16.dp),
                    textAlign = TextAlign.Center,
                    lineHeight = 18.sp
                )
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Permission Items
            PermissionItem(
                title = when (selectedLanguage?.code) { "te" -> "కెమెరా యాక్సెస్"; "ta" -> "கேமரா அணுகல்"; else -> "Camera Access" },
                desc = when (selectedLanguage?.code) { "te" -> "పత్రాలను స్కాన్ చేయడానికి అవసరం"; "ta" -> "ஆவணங்களை ஸ்கேன் செய்ய தேவை"; else -> "Required to scan documents" },
                icon = Icons.Default.PhotoCamera,
                iconColor = Color(0xFF4285F4),
                isGranted = cameraGranted,
                onGrant = { launcher.launch(arrayOf(Manifest.permission.CAMERA)) },
                isDarkMode = isDarkMode,
                isHighContrast = isHighContrast,
                selectedLanguage = selectedLanguage
            )

            Spacer(modifier = Modifier.height(16.dp))

            PermissionItem(
                title = when (selectedLanguage?.code) { "te" -> "స్టోరేజ్ యాక్సెస్"; "ta" -> "சேமிப்பக அணுகல்"; else -> "Storage Access" },
                desc = when (selectedLanguage?.code) { "te" -> "పత్రాలను సేవ్ చేయడానికి అవసరం"; "ta" -> "ஆவணங்களைச் சேமிக்க தேவை"; else -> "Required to save documents" },
                icon = Icons.Default.Folder,
                iconColor = Color(0xFF34A853),
                isGranted = storageGranted,
                onGrant = { launcher.launch(arrayOf(storagePermission)) },
                isDarkMode = isDarkMode,
                isHighContrast = isHighContrast,
                selectedLanguage = selectedLanguage
            )

            Spacer(modifier = Modifier.height(16.dp))

            PermissionItem(
                title = when (selectedLanguage?.code) { "te" -> "మైక్రోఫోన్ యాక్సెస్"; "ta" -> "மைக்ரோஃபோன் அணுகல்"; else -> "Microphone Access" },
                desc = when (selectedLanguage?.code) { "te" -> "వాయిస్ గైడెన్స్ కోసం అవసరం"; "ta" -> "குரல் வழிகாட்டுதலுக்கு தேவை"; else -> "Required for voice guidance" },
                icon = Icons.Default.Mic,
                iconColor = Color(0xFFA142F4),
                isGranted = micGranted,
                onGrant = { launcher.launch(arrayOf(Manifest.permission.RECORD_AUDIO)) },
                isDarkMode = isDarkMode,
                isHighContrast = isHighContrast,
                selectedLanguage = selectedLanguage
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Warning Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF3B2E1E) else Color(0xFFFFF7EF),
                border = BorderStroke(1.dp, if (isHighContrast) Color.White else if (isDarkMode) Color(0xFF5F432A) else Color(0xFFFFE0C1))
            ) {
                val warningStr = when (selectedLanguage?.code) {
                    "te" -> "యాప్ పని చేయడానికి అన్ని అనుమతులు అవసరం"
                    "ta" -> "பயன்பாடு செயல்பட அனைத்து அனுமதிகளும் தேவை"
                    else -> "All permissions are required for the app to work properly"
                }
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Outlined.ErrorOutline, contentDescription = null, tint = if (isHighContrast) Color.Yellow else if (isDarkMode) Color(0xFFF6AD55) else Color(0xFFD97706), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = warningStr, fontSize = 12.sp, color = if (isHighContrast) Color.White else if (isDarkMode) Color(0xFFF6AD55) else Color(0xFFD97706), fontWeight = FontWeight.Medium)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { launcher.launch(arrayOf(Manifest.permission.CAMERA, storagePermission, Manifest.permission.RECORD_AUDIO)) },
                modifier = Modifier.fillMaxWidth().height(52.dp),
                shape = RoundedCornerShape(12.dp),
                contentPadding = PaddingValues(0.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Color.Transparent)
            ) {
                val grantAllStr = when (selectedLanguage?.code) { "te" -> "అన్ని అనుమతులను మంజూరు చేయండి"; "ta" -> "அனைத்து அனுமதிகளையும் வழங்கவும்"; else -> "Grant All Permissions" }
                Box(modifier = Modifier.fillMaxSize().background(brush = Brush.horizontalGradient(colors = listOf(Color(0xFF4285F4), Color(0xFF9333EA))), shape = RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                    Text(text = grantAllStr, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onAllGranted,
                enabled = allGranted,
                modifier = Modifier.fillMaxWidth().height(52.dp).alpha(if (allGranted) 1f else 0.5f),
                shape = RoundedCornerShape(12.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (allGranted) Color(0xFF2196F3) else Color.LightGray,
                    disabledContainerColor = Color.LightGray.copy(alpha = 0.6f)
                )
            ) {
                val continueStr = when (selectedLanguage?.code) { "te" -> "కొనసాగండి"; "ta" -> "தொடரவும்"; else -> "Continue" }
                Text(text = continueStr, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            
            TextButton(
                onClick = {
                    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                        data = Uri.fromParts("package", context.packageName, null)
                    }
                    context.startActivity(intent)
                },
                modifier = Modifier.padding(top = 8.dp)
            ) {
                val settingsStr = when(selectedLanguage?.code) {
                    "te" -> "సెట్టింగ్స్ నుండి అనుమతించండి"
                    "ta" -> "அமைப்புகளிலிருந்து அனுமதிக்கவும்"
                    else -> "Can't see pop-up? Open Settings"
                }
                Text(settingsStr, fontSize = 12.sp, color = if (isHighContrast) Color.Yellow else Color(0xFF2196F3))
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, cardBorder)) {
                val backStr = when (selectedLanguage?.code) { "te" -> "తిరిగి వెళ్ళండి"; "ta" -> "திரும்பி செல்"; else -> "Go Back" }
                Text(text = backStr, color = if (isHighContrast) Color.White else if (isDarkMode) Color.White else Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
            }
            
            Spacer(modifier = Modifier.height(40.dp))
        }
    }
}

@Composable
private fun PermissionItem(
    title: String,
    desc: String,
    icon: ImageVector,
    iconColor: Color,
    isGranted: Boolean,
    onGrant: () -> Unit,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    selectedLanguage: Language?
) {
    val surfaceColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF252525) else Color.White
    val borderColor = if (isHighContrast) Color.White else if (isDarkMode) Color(0xFF333333) else Color(0xFFF3F4F6)
    val titleTextColor = if (isHighContrast) Color.Yellow else if (isDarkMode) Color.White else Color.Black
    val descTextColor = if (isHighContrast) Color.White else if (isDarkMode) Color.LightGray else Color.Gray

    val grantedLabel = when (selectedLanguage?.code) { "te" -> "అనుమతి ఇవ్వబడింది ✅"; "ta" -> "அனுமதி வழங்கப்பட்டது ✅"; else -> "Permission Granted ✅" }
    val grantBtnLabel = when (selectedLanguage?.code) { "te" -> "అనుమతి ఇవ్వండి"; "ta" -> "அனுமதி வழங்கவும்"; else -> "Grant Permission" }

    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        color = surfaceColor,
        border = BorderStroke(1.dp, borderColor),
        shadowElevation = if (isHighContrast) 0.dp else 2.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(40.dp),
                    shape = CircleShape,
                    color = if (isHighContrast) Color.Black else iconColor.copy(alpha = 0.1f),
                    border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = icon, contentDescription = null, tint = if (isHighContrast) Color.White else iconColor, modifier = Modifier.size(20.dp))
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Column(modifier = Modifier.weight(1f)) {
                    Text(text = title, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = titleTextColor)
                    Text(text = desc, fontSize = 12.sp, color = descTextColor)
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            if (isGranted) {
                Row(modifier = Modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.End) {
                    Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF34A853), modifier = Modifier.size(16.dp))
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = grantedLabel, color = Color(0xFF34A853), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = onGrant,
                        modifier = Modifier.height(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isHighContrast) Color.Yellow else Color(0xFF4285F4))
                    ) {
                        Text(text = grantBtnLabel, fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isHighContrast) Color.Black else Color.White)
                    }
                }
            }
        }
    }
}
