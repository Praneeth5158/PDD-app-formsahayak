package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.formsahayak.R
import com.simats.formsahayak.ui.components.BottomNavigationBar
import com.simats.formsahayak.ui.components.InfoRow
import com.simats.formsahayak.ui.components.ProfileActionItem
import com.simats.formsahayak.ui.viewmodel.FormViewModel
import coil.compose.AsyncImage
import com.simats.formsahayak.logic.RetrofitClient

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    userName: String,
    userEmail: String,
    userPhone: String,
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    viewModel: FormViewModel,
    onEditProfileClick: () -> Unit,
    onChangePasswordClick: () -> Unit,
    onAboutDeveloperClick: () -> Unit,
    onLogoutClick: () -> Unit,
    onHomeClick: () -> Unit,
    onFormsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onCameraIconClick: () -> Unit
) {
    val isDark = isDarkMode || isHighContrast
    val backgroundColor = if (isDark) Color.Black else Color(0xFFF8FBFF)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        stringResource(R.string.profile), 
                        fontWeight = FontWeight.ExtraBold, 
                        fontSize = 22.sp, 
                        color = textColor
                    ) 
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = if (isDark) Color.Black else Color.White)
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentScreen = "profile",
                isDarkMode = isDark,
                isHighContrast = isHighContrast,
                selectedLanguage = selectedLanguage,
                onHomeClick = onHomeClick,
                onFormsClick = onFormsClick,
                onHelpClick = onHelpClick,
                onSettingsClick = onSettingsClick,
                onProfileClick = { }
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Info Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = cardColor,
                shadowElevation = if (isHighContrast) 0.dp else 4.dp,
                border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Box(
                        modifier = Modifier.size(110.dp),
                        contentAlignment = Alignment.BottomEnd
                    ) {
                        Surface(
                            modifier = Modifier.size(100.dp),
                            shape = CircleShape,
                            color = Color(0xFFE3F2FD)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                if (viewModel.loggedInUser?.profileImageUrl != null && viewModel.loggedInUser!!.profileImageUrl!!.isNotEmpty()) {
                                    val rawUrl = viewModel.loggedInUser!!.profileImageUrl!!
                                    val fullUrl = if (rawUrl.startsWith("http")) {
                                        try {
                                            RetrofitClient.BASE_URL + java.net.URL(rawUrl).path.replace("\\", "/").removePrefix("/")
                                        } catch(e: Exception) {
                                            rawUrl
                                        }
                                    } else {
                                        RetrofitClient.BASE_URL + rawUrl.replace("\\", "/").removePrefix("/")
                                    }
                                    val cacheBusterUrl = fullUrl + if (fullUrl.contains("?")) "&t=${System.currentTimeMillis()}" else "?t=${System.currentTimeMillis()}"
                                    AsyncImage(
                                        model = coil.request.ImageRequest.Builder(androidx.compose.ui.platform.LocalContext.current)
                                            .data(cacheBusterUrl)
                                            .crossfade(true)
                                            .memoryCachePolicy(coil.request.CachePolicy.DISABLED)
                                            .diskCachePolicy(coil.request.CachePolicy.DISABLED)
                                            .build(),
                                        contentDescription = "Profile Photo",
                                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else if (viewModel.profilePicture != null) {
                                    Image(
                                        bitmap = viewModel.profilePicture!!.asImageBitmap(),
                                        contentDescription = "Profile Photo",
                                        modifier = Modifier.fillMaxSize().clip(CircleShape),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = Color(0xFF2196F3),
                                        modifier = Modifier.size(60.dp)
                                    )
                                }
                            }
                        }
                        
                        // Camera overlay - Clickable
                        Surface(
                            modifier = Modifier
                                .size(36.dp)
                                .offset(x = 4.dp, y = 4.dp)
                                .clickable { onCameraIconClick() },
                            shape = CircleShape,
                            color = Color(0xFF2196F3),
                            border = androidx.compose.foundation.BorderStroke(2.dp, cardColor)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = userName, 
                        fontSize = 22.sp, 
                        fontWeight = FontWeight.ExtraBold, 
                        color = textColor
                    )
                    Text(
                        text = stringResource(R.string.premium_member), 
                        fontSize = 14.sp, 
                        color = if (isDark) Color.LightGray else Color.Gray
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Personal Information Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = cardColor,
                shadowElevation = if (isHighContrast) 0.dp else 4.dp,
                border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = stringResource(R.string.personal_info), 
                        fontSize = 16.sp, 
                        fontWeight = FontWeight.ExtraBold, 
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    InfoRow(icon = Icons.Default.Email, label = stringResource(R.string.email), value = userEmail, iconColor = Color(0xFF2196F3), isDarkMode = isDark)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = if (isDark) Color(0xFF333333) else Color(0xFFF0F0F0))
                    InfoRow(icon = Icons.Default.Phone, label = stringResource(R.string.phone_number), value = userPhone, iconColor = Color(0xFF4CAF50), isDarkMode = isDark)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = if (isDark) Color(0xFF333333) else Color(0xFFF0F0F0))
                    InfoRow(icon = Icons.Default.Translate, label = stringResource(R.string.lang_pref), value = selectedLanguage?.name ?: "English", iconColor = Color(0xFF9C27B0), isDarkMode = isDark)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Action List
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(20.dp),
                color = cardColor,
                shadowElevation = if (isHighContrast) 0.dp else 2.dp,
                border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
            ) {
                Column {
                    ProfileActionItem(
                        icon = Icons.Default.Edit, 
                        label = stringResource(R.string.edit_profile), 
                        isDarkMode = isDark,
                        onClick = onEditProfileClick
                    )
                    HorizontalDivider(color = if (isDark) Color(0xFF333333) else Color(0xFFF5F5F5))
                    ProfileActionItem(
                        icon = Icons.Default.Lock, 
                        label = stringResource(R.string.change_password), 
                        isDarkMode = isDark,
                        onClick = onChangePasswordClick
                    )
                    HorizontalDivider(color = if (isDark) Color(0xFF333333) else Color(0xFFF5F5F5))
                    ProfileActionItem(
                        icon = Icons.Default.Info, 
                        label = stringResource(R.string.about_developer), 
                        isDarkMode = isDark,
                        onClick = onAboutDeveloperClick
                    )
                    HorizontalDivider(color = if (isDark) Color(0xFF333333) else Color(0xFFF5F5F5))
                    ProfileActionItem(
                        icon = Icons.AutoMirrored.Filled.Logout, 
                        label = stringResource(R.string.logout),
                        color = Color.Red, 
                        isDarkMode = isDark,
                        onClick = onLogoutClick
                    )
                }
            }
        }
    }
}
