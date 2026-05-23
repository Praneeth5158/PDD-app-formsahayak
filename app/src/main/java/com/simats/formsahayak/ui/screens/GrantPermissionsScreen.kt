package com.simats.formsahayak.ui.screens

import android.Manifest
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
import androidx.compose.ui.res.stringResource
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
import com.simats.formsahayak.R

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
    var showSettingsDialog by remember { mutableStateOf(false) }

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
            val activity = context as? android.app.Activity
            val showRationaleCamera = activity?.let { androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.CAMERA) } ?: true
            val showRationaleStorage = activity?.let { androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(it, storagePermission) } ?: true
            val showRationaleMic = activity?.let { androidx.core.app.ActivityCompat.shouldShowRequestPermissionRationale(it, Manifest.permission.RECORD_AUDIO) } ?: true
            
            val cameraPermanentlyDenied = !cameraGranted && !showRationaleCamera
            val storagePermanentlyDenied = !storageGranted && !showRationaleStorage
            val micPermanentlyDenied = !micGranted && !showRationaleMic
            
            if (cameraPermanentlyDenied || storagePermanentlyDenied || micPermanentlyDenied) {
                showSettingsDialog = true
            } else {
                Toast.makeText(context, context.getString(R.string.perm_required_toast), Toast.LENGTH_SHORT).show()
            }
        }
    }

    val allGranted = cameraGranted && storageGranted && micGranted

    val backgroundColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF121212) else Color(0xFFF8FBFF)
    val textColor = if (isHighContrast) Color.Yellow else if (isDarkMode) Color.White else Color.Black
    val subTextColor = if (isHighContrast) Color.White else if (isDarkMode) Color.LightGray else Color.Gray
    val cardBorder = if (isHighContrast) Color.White else if (isDarkMode) Color(0xFF333333) else Color(0xFFE5E7EB)
    val cardColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF1E1E1E) else Color.White

    Surface(modifier = Modifier.fillMaxSize(), color = backgroundColor) {
        if (showSettingsDialog) {
            AlertDialog(
                onDismissRequest = { showSettingsDialog = false },
                title = { Text(stringResource(R.string.perm_required_title), fontWeight = FontWeight.Bold, color = textColor) },
                text = { Text(stringResource(R.string.perm_permanently_denied), color = textColor) },
                confirmButton = {
                    TextButton(
                        onClick = {
                            showSettingsDialog = false
                            val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
                                data = Uri.fromParts("package", context.packageName, null)
                            }
                            context.startActivity(intent)
                        }
                    ) {
                        Text(stringResource(R.string.open_settings), fontWeight = FontWeight.Bold)
                    }
                },
                dismissButton = {
                    TextButton(onClick = { showSettingsDialog = false }) {
                        Text(stringResource(R.string.cancel))
                    }
                },
                containerColor = cardColor
            )
        }

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
                text = stringResource(R.string.grant_permissions),
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = stringResource(R.string.perm_desc),
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
                Text(
                    text = buildAnnotatedString {
                        withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = if (isHighContrast) Color.Yellow else if (isDarkMode) Color(0xFF63B3ED) else Color(0xFF1967D2))) {
                            append(stringResource(R.string.privacy_first))
                        }
                        append(stringResource(R.string.privacy_desc))
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
                title = stringResource(R.string.camera_access),
                desc = stringResource(R.string.camera_desc),
                icon = Icons.Default.PhotoCamera,
                iconColor = Color(0xFF4285F4),
                isGranted = cameraGranted,
                onGrant = { launcher.launch(arrayOf(Manifest.permission.CAMERA)) },
                isDarkMode = isDarkMode,
                isHighContrast = isHighContrast
            )

            Spacer(modifier = Modifier.height(16.dp))

            PermissionItem(
                title = stringResource(R.string.storage_access),
                desc = stringResource(R.string.storage_desc),
                icon = Icons.Default.Folder,
                iconColor = Color(0xFF34A853),
                isGranted = storageGranted,
                onGrant = { launcher.launch(arrayOf(storagePermission)) },
                isDarkMode = isDarkMode,
                isHighContrast = isHighContrast
            )

            Spacer(modifier = Modifier.height(16.dp))

            PermissionItem(
                title = stringResource(R.string.mic_access),
                desc = stringResource(R.string.mic_desc),
                icon = Icons.Default.Mic,
                iconColor = Color(0xFFA142F4),
                isGranted = micGranted,
                onGrant = { launcher.launch(arrayOf(Manifest.permission.RECORD_AUDIO)) },
                isDarkMode = isDarkMode,
                isHighContrast = isHighContrast
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Warning Box
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                color = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF3B2E1E) else Color(0xFFFFF7EF),
                border = BorderStroke(1.dp, if (isHighContrast) Color.White else if (isDarkMode) Color(0xFF5F432A) else Color(0xFFFFE0C1))
            ) {
                Row(modifier = Modifier.padding(12.dp), verticalAlignment = Alignment.CenterVertically) {
                    Icon(imageVector = Icons.Outlined.ErrorOutline, contentDescription = null, tint = if (isHighContrast) Color.Yellow else if (isDarkMode) Color(0xFFF6AD55) else Color(0xFFD97706), modifier = Modifier.size(20.dp))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = stringResource(R.string.perm_required_warning), fontSize = 12.sp, color = if (isHighContrast) Color.White else if (isDarkMode) Color(0xFFF6AD55) else Color(0xFFD97706), fontWeight = FontWeight.Medium)
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
                Box(modifier = Modifier.fillMaxSize().background(brush = Brush.horizontalGradient(colors = listOf(Color(0xFF4285F4), Color(0xFF9333EA))), shape = RoundedCornerShape(12.dp)), contentAlignment = Alignment.Center) {
                    Text(text = stringResource(R.string.grant_all_perm), color = Color.White, fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                Text(text = stringResource(R.string.continue_btn), fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
                Text(stringResource(R.string.open_settings), fontSize = 12.sp, color = if (isHighContrast) Color.Yellow else Color(0xFF2196F3))
            }

            Spacer(modifier = Modifier.height(12.dp))

            OutlinedButton(onClick = onBack, modifier = Modifier.fillMaxWidth().height(52.dp), shape = RoundedCornerShape(12.dp), border = BorderStroke(1.dp, cardBorder)) {
                Text(text = stringResource(R.string.go_back), color = if (isHighContrast) Color.White else if (isDarkMode) Color.White else Color.Black, fontWeight = FontWeight.Bold, fontSize = 16.sp)
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
    isHighContrast: Boolean
) {
    val surfaceColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF252525) else Color.White
    val borderColor = if (isHighContrast) Color.White else if (isDarkMode) Color(0xFF333333) else Color(0xFFF3F4F6)
    val titleTextColor = if (isHighContrast) Color.Yellow else if (isDarkMode) Color.White else Color.Black
    val descTextColor = if (isHighContrast) Color.White else if (isDarkMode) Color.LightGray else Color.Gray

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
                    Text(text = stringResource(R.string.perm_granted), color = Color(0xFF34A853), fontSize = 14.sp, fontWeight = FontWeight.Medium)
                }
            } else {
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.End) {
                    Button(
                        onClick = onGrant,
                        modifier = Modifier.height(36.dp),
                        shape = RoundedCornerShape(8.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = if (isHighContrast) Color.Yellow else Color(0xFF4285F4))
                    ) {
                        Text(text = stringResource(R.string.grant_perm_btn), fontSize = 12.sp, fontWeight = FontWeight.Bold, color = if (isHighContrast) Color.Black else Color.White)
                    }
                }
            }
        }
    }
}
