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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.formsahayak.ui.components.BottomNavigationBar
import com.simats.formsahayak.ui.components.InfoRow
import com.simats.formsahayak.ui.components.ProfileActionItem
import com.simats.formsahayak.ui.viewmodel.FormViewModel

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

    // Translation logic
    val title = when (selectedLanguage?.code) {
        "te" -> "ప్రొఫైల్"
        "ta" -> "சுயவிவரம்"
        else -> "Profile"
    }
    val memberStatus = when (selectedLanguage?.code) {
        "te" -> "ప్రీమియం సభ్యుడు"
        "ta" -> "பிரீமியம் உறுப்பினர்"
        else -> "Premium Member"
    }
    val personalInfoTitle = when (selectedLanguage?.code) {
        "te" -> "వ్యక్తిగత సమాచారం"
        "ta" -> "தனிப்பட்ட தகவல்"
        else -> "Personal Information"
    }
    val emailLabel = when (selectedLanguage?.code) {
        "te" -> "ఇమెయిల్"
        "ta" -> "மின்னஞ்சல்"
        else -> "Email"
    }
    val phoneLabel = when (selectedLanguage?.code) {
        "te" -> "ఫోన్ నంబర్"
        "ta" -> "தொலைபேசி எண்"
        else -> "Phone Number"
    }
    val languageLabel = when (selectedLanguage?.code) {
        "te" -> "భాషా ప్రాధాన్యత"
        "ta" -> "மொழி விருப்பம்"
        else -> "Language Preference"
    }
    val editProfileLabel = when (selectedLanguage?.code) {
        "te" -> "ప్రొఫైల్ సవరించు"
        "ta" -> "சுயவிவரத்தைத் திருத்து"
        else -> "Edit Profile"
    }
    val changePasswordLabel = when (selectedLanguage?.code) {
        "te" -> "పాస్‌వర్డ్ మార్చండి"
        "ta" -> "கடவுச்சொல்லை மாற்றவும்"
        else -> "Change Password"
    }
    val logoutLabel = when (selectedLanguage?.code) {
        "te" -> "లాగ్ అవుట్"
        "ta" -> "வெளியேறு"
        else -> "Logout"
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { 
                    Text(
                        title, 
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
                                if (viewModel.profilePicture != null) {
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
                        text = memberStatus, 
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
                        text = personalInfoTitle, 
                        fontSize = 16.sp, 
                        fontWeight = FontWeight.ExtraBold, 
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    InfoRow(icon = Icons.Default.Email, label = emailLabel, value = userEmail, iconColor = Color(0xFF2196F3), isDarkMode = isDark)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = if (isDark) Color(0xFF333333) else Color(0xFFF0F0F0))
                    InfoRow(icon = Icons.Default.Phone, label = phoneLabel, value = userPhone, iconColor = Color(0xFF4CAF50), isDarkMode = isDark)
                    HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = if (isDark) Color(0xFF333333) else Color(0xFFF0F0F0))
                    InfoRow(icon = Icons.Default.Translate, label = languageLabel, value = selectedLanguage?.name ?: "English", iconColor = Color(0xFF9C27B0), isDarkMode = isDark)
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
                        label = editProfileLabel, 
                        isDarkMode = isDark,
                        onClick = onEditProfileClick
                    )
                    HorizontalDivider(color = if (isDark) Color(0xFF333333) else Color(0xFFF5F5F5))
                    ProfileActionItem(
                        icon = Icons.Default.Lock, 
                        label = changePasswordLabel, 
                        isDarkMode = isDark,
                        onClick = onChangePasswordClick
                    )
                    HorizontalDivider(color = if (isDark) Color(0xFF333333) else Color(0xFFF5F5F5))
                    ProfileActionItem(
                        icon = Icons.AutoMirrored.Filled.Logout, 
                        label = logoutLabel, 
                        color = Color.Red, 
                        isDarkMode = isDark,
                        onClick = onLogoutClick
                    )
                }
            }
        }
    }
}
