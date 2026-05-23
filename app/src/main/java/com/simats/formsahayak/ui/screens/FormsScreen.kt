package com.simats.formsahayak.ui.screens

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Download
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.CachePolicy
import coil.request.ImageRequest
import com.simats.formsahayak.R
import com.simats.formsahayak.logic.HistoryItem
import com.simats.formsahayak.logic.RetrofitClient
import com.simats.formsahayak.logic.UrlHelper
import com.simats.formsahayak.ui.components.BottomNavigationBar
import com.simats.formsahayak.ui.viewmodel.FormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormsScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    viewModel: FormViewModel,
    onScanSelected: () -> Unit,
    onHomeClick: () -> Unit,
    onHelpClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onProfileClick: () -> Unit,
    onFormDetailsClick: (Int) -> Unit
) {

    val context = LocalContext.current
    val isDark = isDarkMode || isHighContrast

    val backgroundColor = when {
        isHighContrast -> Color.Black
        isDarkMode -> Color(0xFF121212)
        else -> Color(0xFFF8FBFF)
    }

    val textColor = when {
        isHighContrast -> Color.Yellow
        isDarkMode -> Color.White
        else -> Color.Black
    }

    val cardColor = when {
        isHighContrast -> Color.Black
        isDarkMode -> Color(0xFF1E1E1E)
        else -> Color.White
    }

    Log.d("FormsScreen", "Rendering FormsScreen. History count: ${viewModel.formsHistory.size}")

    LaunchedEffect(Unit) {
        val email = viewModel.loggedInUser?.emailOrPhone

        Log.d("FormsScreen", "LaunchedEffect: Fetching history for $email")

        if (!email.isNullOrEmpty()) {
            viewModel.fetchHistory(email)
        }
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.my_forms),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 22.sp,
                        color = textColor
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = if (isDark) Color.Black else Color.White
                )
            )
        },

        bottomBar = {
            BottomNavigationBar(
                currentScreen = "forms",
                isDarkMode = isDarkMode,
                isHighContrast = isHighContrast,
                selectedLanguage = selectedLanguage,
                onHomeClick = onHomeClick,
                onFormsClick = { },
                onHelpClick = helpClick@ { onHelpClick() },
                onSettingsClick = { onSettingsClick() },
                onProfileClick = { onProfileClick() }
            )
        },

        containerColor = backgroundColor

    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(backgroundColor)
                .padding(16.dp)
        ) {

            OutlinedTextField(
                value = "",
                onValueChange = {},
                placeholder = {
                    Text(
                        stringResource(R.string.search_forms),
                        color = if (isHighContrast) Color.White else Color.Gray
                    )
                },

                leadingIcon = {
                    Icon(
                        Icons.Default.Search,
                        contentDescription = null,
                        tint = if (isHighContrast) Color.Yellow else Color.Gray
                    )
                },

                modifier = Modifier.fillMaxWidth(),

                shape = RoundedCornerShape(12.dp),

                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    focusedContainerColor = cardColor,
                    unfocusedContainerColor = cardColor,
                    focusedBorderColor = if (isHighContrast)
                        Color.Yellow
                    else
                        MaterialTheme.colorScheme.primary,

                    unfocusedBorderColor = if (isHighContrast)
                        Color.White
                    else
                        Color.LightGray
                )
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                stringResource(R.string.forms_history),
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            Spacer(modifier = Modifier.height(16.dp))

            if (viewModel.formsHistory.isEmpty()) {

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),

                    contentAlignment = Alignment.Center
                ) {

                    Text(
                        text = stringResource(R.string.no_history),

                        color = if (isHighContrast)
                            Color.White
                        else
                            Color.Gray
                    )
                }

            } else {

                LazyColumn(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxWidth(),

                    verticalArrangement = Arrangement.spacedBy(12.dp),

                    contentPadding = PaddingValues(bottom = 16.dp)

                ) {

                    items(viewModel.formsHistory) { item ->

                        val isPlaying =
                            viewModel.isAudioPlaying &&
                                    viewModel.currentlyPlayingUrl == item.audioPath

                        HistoryCard(
                            item = item,
                            textColor = textColor,
                            cardColor = cardColor,
                            isHighContrast = isHighContrast,
                            isPlaying = isPlaying,

                            onPlayAudio = {
                                viewModel.playHistoryAudio(item.audioPath)
                            },

                            onStopAudio = {
                                viewModel.stopAudio()
                            },

                            onClick = {
                                item.id?.let {
                                    onFormDetailsClick(it)
                                }
                            },

                            onDeleteClick = {

                                item.id?.let { docId ->

                                    viewModel.deleteHistory(docId) { success, message ->

                                        Log.d(
                                            "DeleteHistory",
                                            message
                                        )
                                    }
                                }
                            },

                            onDownloadClick = { url ->
                                viewModel.downloadSpecificPdf(context, url)
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryCard(
    item: HistoryItem,
    textColor: Color,
    cardColor: Color,
    isHighContrast: Boolean,
    isPlaying: Boolean,
    onPlayAudio: () -> Unit,
    onStopAudio: () -> Unit,
    onClick: () -> Unit,
    onDeleteClick: () -> Unit,
    onDownloadClick: (String?) -> Unit
) {
    val context = LocalContext.current
    val imageUrl = item.fileUrl
    val fullImageUrl = remember(imageUrl) {
        if (imageUrl.isNullOrEmpty()) null 
        else {
            val clean = UrlHelper.cleanUrl(imageUrl)
            val full = "$clean?t=${System.currentTimeMillis()}"
            Log.d("FormsScreen", "image URL received: $full")
            full
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },

        shape = RoundedCornerShape(16.dp),

        color = cardColor,

        shadowElevation = if (isHighContrast)
            0.dp
        else
            2.dp,

        border = if (isHighContrast)
            BorderStroke(2.dp, Color.Yellow)
        else
            null
    ) {

        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            
            // Image Preview instead of Icon
            Box(
                modifier = Modifier
                    .size(64.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.LightGray.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                if (fullImageUrl != null) {
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
                        contentScale = ContentScale.Crop,
                        onLoading = { Log.d("FormsScreen", "IMAGE_RENDER_LOG: Loading URL: $fullImageUrl") },
                        onSuccess = { 
                            Log.d("FormsScreen", "IMAGE_RENDER_LOG: Success URL: $fullImageUrl")
                            Log.d("FormsScreen", "image loading success")
                        },
                        onError = { error -> 
                            Log.e("FormsScreen", "IMAGE_RENDER_LOG: Failed URL: $fullImageUrl, Error: ${error.result.throwable.message}")
                            Log.e("FormsScreen", "image loading failure: ${error.result.throwable.message}")
                        }
                    )
                } else {
                    Icon(
                        Icons.Default.Description,
                        contentDescription = null,
                        tint = if (isHighContrast) Color.Yellow else Color(0xFF2196F3),
                        modifier = Modifier.size(32.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {

                Text(
                    item.fileName ?: "Unknown Form",
                    fontWeight = FontWeight.Bold,
                    color = textColor,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis
                )

                Text(
                    item.createdAt ?: "",
                    fontSize = 11.sp,
                    color = if (isHighContrast)
                        Color.White
                    else
                        Color.Gray
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    item.guidanceText ?: "",
                    fontSize = 12.sp,
                    color = textColor,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                if (!item.audioPath.isNullOrEmpty()) {
                    IconButton(
                        onClick = if (isPlaying) onStopAudio else onPlayAudio,
                        modifier = Modifier.size(32.dp)
                    ) {
                        Icon(
                            if (isPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                            contentDescription = if (isPlaying) "Stop Audio" else "Play Audio",
                            tint = if (isHighContrast) Color.Yellow else if (isPlaying) Color.Red else Color(0xFF4CAF50),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                IconButton(
                    onClick = {
                        val fallbackUrl = item.pdfFile ?: item.pdfPath ?: item.pdfUrl ?: item.audioPath?.replace("audio", "pdf")?.replace(".mp3", ".pdf")
                        onDownloadClick(fallbackUrl)
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Download,
                        contentDescription = "Download PDF",
                        tint = if (isHighContrast) Color.Yellow else Color(0xFF2196F3),
                        modifier = Modifier.size(20.dp)
                    )
                }

                IconButton(
                    onClick = onDeleteClick,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Delete",
                        tint = Color.Red,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
        }
    }
}