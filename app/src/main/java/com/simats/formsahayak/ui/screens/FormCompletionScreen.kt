package com.simats.formsahayak.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
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
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Upload
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import com.simats.formsahayak.R
import com.simats.formsahayak.ui.viewmodel.FormViewModel

@Composable
fun FormCompletionScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    viewModel: FormViewModel,
    onFillAnotherClick: () -> Unit,
    onBackToHomeClick: () -> Unit
) {

    val context = LocalContext.current

    val storagePermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            viewModel.downloadSpecificPdf(context, viewModel.backendPdfUrl)
        } else {
            Toast.makeText(context, context.getString(R.string.storage_denied), Toast.LENGTH_SHORT).show()
        }
    }

    val backgroundColor =
        if (isHighContrast) Color.Black
        else if (isDarkMode) Color(0xFF121212)
        else Color(0xFFF8FBFF)

    val cardColor =
        if (isHighContrast) Color.Black
        else if (isDarkMode) Color(0xFF1E1E1E)
        else Color.White

    val textColor =
        if (isDarkMode || isHighContrast) Color.White
        else Color(0xFF2C3E50)

    val secondaryTextColor =
        if (isDarkMode || isHighContrast) Color.LightGray
        else Color.Gray

    val nextStepsBgColor =
        if (isDarkMode) Color(0xFF2A2A2A)
        else Color(0xFFF4F8FD)

    val steps = listOf(
        stringResource(R.string.step_fill_physical),
        stringResource(R.string.step_double_check),
        stringResource(R.string.step_sign),
        stringResource(R.string.step_submit)
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor),
        contentAlignment = Alignment.Center
    ) {

        Surface(
            modifier = Modifier
                .fillMaxWidth(0.9f)
                .wrapContentHeight(),

            shape = RoundedCornerShape(24.dp),

            color = cardColor,

            shadowElevation =
                if (isHighContrast) 0.dp
                else 8.dp,

            border =
                if (isHighContrast)
                    androidx.compose.foundation.BorderStroke(
                        2.dp,
                        Color.White
                    )
                else null
        ) {

            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState()),

                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                Surface(
                    modifier = Modifier.size(100.dp),

                    shape = CircleShape,

                    color = Color(0xFFE8F5E9)
                ) {

                    Box(
                        contentAlignment = Alignment.Center
                    ) {

                        Icon(
                            imageVector = Icons.Default.Check,

                            contentDescription = null,

                            tint = Color(0xFF4CAF50),

                            modifier = Modifier.size(60.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.success),

                    fontSize = 28.sp,

                    fontWeight = FontWeight.Bold,

                    color = textColor,

                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = stringResource(R.string.completion_desc),

                    fontSize = 15.sp,

                    color = secondaryTextColor,

                    textAlign = TextAlign.Center,

                    lineHeight = 22.sp
                )

                Spacer(modifier = Modifier.height(24.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),

                    shape = RoundedCornerShape(16.dp),

                    color = nextStepsBgColor
                ) {

                    Column(
                        modifier = Modifier.padding(20.dp)
                    ) {

                        Text(
                            text = stringResource(R.string.next_steps),

                            fontSize = 16.sp,

                            fontWeight = FontWeight.Bold,

                            color = textColor
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        steps.forEach { step ->

                            Row(
                                verticalAlignment = Alignment.CenterVertically,

                                modifier = Modifier.padding(vertical = 6.dp)
                            ) {

                                Icon(
                                    imageVector = Icons.Default.Check,

                                    contentDescription = null,

                                    tint = Color(0xFF4CAF50),

                                    modifier = Modifier.size(16.dp)
                                )

                                Spacer(modifier = Modifier.width(8.dp))

                                Text(
                                    text = step,

                                    fontSize = 14.sp,

                                    color = secondaryTextColor
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = onBackToHomeClick,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),

                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6)
                    ),

                    shape = RoundedCornerShape(12.dp)
                ) {

                    Icon(
                        Icons.Default.Home,

                        contentDescription = null,

                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        stringResource(R.string.return_home),

                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                            viewModel.downloadSpecificPdf(context, viewModel.backendPdfUrl)
                        } else {
                            if (ContextCompat.checkSelfPermission(context, Manifest.permission.WRITE_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED) {
                                viewModel.downloadSpecificPdf(context, viewModel.backendPdfUrl)
                            } else {
                                storagePermissionLauncher.launch(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            }
                        }
                    },
                    enabled = !viewModel.isBackendProcessing && !viewModel.backendPdfUrl.isNullOrEmpty(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFF3B82F6),
                        disabledContainerColor = Color.Gray
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {

                    Icon(
                        Icons.Default.Download,

                        contentDescription = null,

                        modifier = Modifier.size(18.dp)
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    val downloadText = if (viewModel.isBackendProcessing) stringResource(R.string.processing) else stringResource(R.string.download_pdf)
                    Text(
                        downloadText,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                OutlinedButton(
                    onClick = onFillAnotherClick,

                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),

                    shape = RoundedCornerShape(12.dp),

                    border = androidx.compose.foundation.BorderStroke(
                        1.dp,

                        if (isDarkMode || isHighContrast)
                            Color.White
                        else
                            Color(0xFFEEEEEE)
                    )
                ) {

                    Icon(
                        Icons.Default.Upload,

                        contentDescription = null,

                        modifier = Modifier.size(18.dp),

                        tint = textColor
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        stringResource(R.string.upload_another),

                        fontWeight = FontWeight.Bold,

                        color = textColor
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(
                    text = stringResource(R.string.share_fs),

                    fontSize = 13.sp,

                    color = secondaryTextColor,

                    textAlign = TextAlign.Center,

                    lineHeight = 20.sp
                )
            }
        }
    }
}
