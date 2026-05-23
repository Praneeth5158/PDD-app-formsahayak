package com.simats.formsahayak.ui.screens

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.simats.formsahayak.R
import com.simats.formsahayak.logic.RetrofitClient
import com.simats.formsahayak.logic.UrlHelper
import com.simats.formsahayak.ui.viewmodel.FormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDetailsScreen(
    viewModel: FormViewModel,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onBack: () -> Unit
) {
    val details = viewModel.selectedFormDetails
    val isDark = isDarkMode || isHighContrast
    val backgroundColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF121212) else Color(0xFFF8FBFF)
    val textColor = if (isHighContrast) Color.Yellow else if (isDarkMode) Color.White else Color.Black
    val cardColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF1E1E1E) else Color.White

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.form_details), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.AutoMirrored.Filled.ArrowBack, contentDescription = stringResource(R.string.back))
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = if (isDark) Color.Black else Color.White,
                    titleContentColor = textColor,
                    navigationIconContentColor = textColor
                )
            )
        },
        containerColor = backgroundColor
    ) { padding ->
        if (details == null) {
            Box(modifier = Modifier.fillMaxSize().padding(padding), contentAlignment = Alignment.Center) {
                if (viewModel.isLoading) {
                    CircularProgressIndicator()
                } else {
                    Text(stringResource(R.string.no_details_found), color = textColor)
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(padding)
                    .verticalScroll(rememberScrollState())
                    .padding(16.dp)
            ) {
                // Form Image Preview
                val imageUrl = details.fileUrl
                val fullImageUrl = remember(imageUrl) {
                    if (imageUrl.isNullOrEmpty()) null 
                    else {
                        val clean = UrlHelper.cleanUrl(imageUrl)
                        val full = "$clean?t=${System.currentTimeMillis()}"
                        Log.d("FormDetailsScreen", "image URL received: $full")
                        full
                    }
                }
                
                Log.d("FormDetailsScreen", "IMAGE_RENDER_LOG: Attempting to load URL: $fullImageUrl")

                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(350.dp)
                        .clip(RoundedCornerShape(12.dp)),
                    color = Color.LightGray.copy(alpha = 0.2f)
                ) {
                    if (fullImageUrl != null) {
                        val context = LocalContext.current
                        val imageRequest = remember(fullImageUrl) {
                            ImageRequest.Builder(context)
                                .data(fullImageUrl)
                                .memoryCachePolicy(CachePolicy.DISABLED)
                                .diskCachePolicy(CachePolicy.DISABLED)
                                .build()
                        }
                        
                        AsyncImage(
                            model = imageRequest,
                            contentDescription = "Form Preview",
                            modifier = Modifier.fillMaxSize(),
                            contentScale = ContentScale.Fit,
                            onLoading = { Log.d("FormDetailsScreen", "IMAGE_RENDER_LOG: Loading... URL: $fullImageUrl") },
                            onSuccess = { 
                                Log.d("FormDetailsScreen", "IMAGE_RENDER_LOG: Success loading URL: $fullImageUrl")
                                Log.d("FormDetailsScreen", "image loading success")
                            },
                            onError = { error -> 
                                Log.e("FormDetailsScreen", "IMAGE_RENDER_LOG: Failed URL: $fullImageUrl, Error: ${error.result.throwable.message}")
                                Log.e("FormDetailsScreen", "image loading failure: ${error.result.throwable.message}")
                            }
                        )
                    } else {
                        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                            Text(stringResource(R.string.no_image_url), color = Color.Gray)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text(details.fileName, fontSize = 22.sp, fontWeight = FontWeight.ExtraBold, color = textColor)
                Text(details.createdAt ?: "", fontSize = 14.sp, color = Color.Gray)

                Spacer(modifier = Modifier.height(24.dp))

                // Guidance Section
                Card(
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(stringResource(R.string.ai_guidance), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(details.guidanceText ?: stringResource(R.string.no_guidance_available), color = textColor)
                        
                        if (!details.audioPath.isNullOrEmpty()) {
                            Spacer(modifier = Modifier.height(16.dp))
                            val isPlaying = viewModel.isAudioPlaying && viewModel.currentlyPlayingUrl == details.audioPath
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Button(
                                    onClick = { 
                                        if (isPlaying) viewModel.stopAudio() 
                                        else viewModel.playHistoryAudio(details.audioPath) 
                                    },
                                    colors = ButtonDefaults.buttonColors(
                                        containerColor = if (isPlaying) Color.Red else MaterialTheme.colorScheme.primary
                                    ),
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Icon(if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow, null)
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(if (isPlaying) stringResource(R.string.stop_audio) else stringResource(R.string.play_audio_guidance))
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Extracted Text Section
                Card(
                    colors = CardDefaults.cardColors(containerColor = cardColor),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(stringResource(R.string.extracted_text), fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(details.extractedText ?: stringResource(R.string.no_text_extracted), color = textColor)
                    }
                }
            }
        }
    }
}
