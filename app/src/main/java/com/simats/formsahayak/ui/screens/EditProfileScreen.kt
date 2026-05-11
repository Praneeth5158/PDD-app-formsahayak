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
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.formsahayak.ui.viewmodel.FormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditProfileScreen(
    currentName: String,
    currentEmail: String,
    currentPhone: String,
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    viewModel: FormViewModel,
    onLanguageChange: (Language) -> Unit,
    onBackClick: () -> Unit,
    onSaveClick: (String, String, String) -> Unit,
    onChangePhotoClick: () -> Unit
) {
    val isDark = isDarkMode || isHighContrast
    val backgroundColor = if (isDark) Color.Black else Color(0xFFF8FBFF)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    val languages = listOf(
        Language("English", "English", "en"),
        Language("Telugu", "తెలుగు", "te"),
        Language("Tamil", "தமிழ்", "ta")
    )

    var fullName by remember { mutableStateOf(currentName) }
    var email by remember { mutableStateOf(currentEmail) }
    var phone by remember { mutableStateOf(currentPhone) }

    // Translation logic
    val titleText = when (selectedLanguage?.code) {
        "te" -> "ప్రొఫైల్ సవరించు"
        "ta" -> "சுயவிவரத்தைத் திருத்து"
        else -> "Edit Profile"
    }
    val fullNameLabel = when (selectedLanguage?.code) {
        "te" -> "పూర్తి పేరు"
        "ta" -> "முழு பெயர்"
        else -> "Full Name"
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
    val saveButtonText = when (selectedLanguage?.code) {
        "te" -> "మార్పులను సేవ్ చేయండి"
        "ta" -> "மாற்றங்களைச் சேமிக்கவும்"
        else -> "Save Changes"
    }
    val cancelButtonText = when (selectedLanguage?.code) {
        "te" -> "రద్దు చేయండి"
        "ta" -> "ரத்துசெய்"
        else -> "Cancel"
    }
    val changePhotoText = when (selectedLanguage?.code) {
        "te" -> "ఫోటో మార్చండి"
        "ta" -> "புகைப்படத்தை மாற்றவும்"
        else -> "Change Photo"
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(titleText, fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = textColor) },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Back", tint = textColor)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = if (isDark) Color.Black else Color.White)
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
                .padding(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Header / Avatar Preview with Change option
            Box(
                modifier = Modifier.size(110.dp),
                contentAlignment = Alignment.BottomEnd
            ) {
                Surface(
                    modifier = Modifier.size(100.dp),
                    shape = CircleShape,
                    color = if (isDark) Color(0xFF333333) else Color(0xFFE3F2FD),
                    border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
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
                
                // Camera overlay
                Surface(
                    modifier = Modifier
                        .size(32.dp)
                        .clickable { onChangePhotoClick() },
                    shape = CircleShape,
                    color = Color(0xFF2196F3),
                    border = androidx.compose.foundation.BorderStroke(2.dp, cardColor)
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            tint = Color.White,
                            modifier = Modifier.size(16.dp)
                        )
                    }
                }
            }

            TextButton(onClick = onChangePhotoClick) {
                Text(text = changePhotoText, color = Color(0xFF2196F3), fontWeight = FontWeight.Bold)
            }
            
            Spacer(modifier = Modifier.height(24.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = cardColor,
                shadowElevation = if (isHighContrast) 0.dp else 2.dp,
                border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    EditField(label = fullNameLabel, value = fullName, onValueChange = { fullName = it }, icon = Icons.Default.Person, isDarkMode = isDark)
                    Spacer(modifier = Modifier.height(20.dp))
                    EditField(label = emailLabel, value = email, onValueChange = { email = it }, icon = Icons.Default.Email, isDarkMode = isDark)
                    Spacer(modifier = Modifier.height(20.dp))
                    EditField(label = phoneLabel, value = phone, onValueChange = { phone = it }, icon = Icons.Default.Phone, isDarkMode = isDark)
                    
                    Spacer(modifier = Modifier.height(24.dp))
                    
                    // Language Selection Toggle (Segmented-like)
                    Text(text = languageLabel, fontSize = 14.sp, fontWeight = FontWeight.Bold, color = textColor)
                    Spacer(modifier = Modifier.height(12.dp))
                    
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(if (isDark) Color(0xFF333333) else Color(0xFFF0F0F0))
                            .padding(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        languages.forEach { lang ->
                            val isSelected = selectedLanguage?.code == lang.code
                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .fillMaxHeight()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) Color(0xFF2196F3) else Color.Transparent)
                                    .clickable { onLanguageChange(lang) },
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = lang.nativeName,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                    color = if (isSelected) Color.White else (if (isDark) Color.LightGray else Color.DarkGray)
                                )
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = { onSaveClick(fullName, email, phone) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
                    ) {
                        Text(saveButtonText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    OutlinedButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) Color.White else Color.LightGray)
                    ) {
                        Text(cancelButtonText, fontSize = 16.sp, color = if (isDark) Color.White else Color.Gray)
                    }
                }
            }
        }
    }
}

@Composable
fun EditField(label: String, value: String, onValueChange: (String) -> Unit, icon: androidx.compose.ui.graphics.vector.ImageVector, isDarkMode: Boolean) {
    Column {
        Text(text = label, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = if (isDarkMode) Color.White else Color.Black)
        Spacer(modifier = Modifier.height(8.dp))
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(imageVector = icon, contentDescription = null, tint = if (isDarkMode) Color.Gray else Color.LightGray) },
            shape = RoundedCornerShape(12.dp),
            colors = OutlinedTextFieldDefaults.colors(
                focusedTextColor = if (isDarkMode) Color.White else Color.Black,
                unfocusedTextColor = if (isDarkMode) Color.White else Color.Black,
                focusedBorderColor = Color(0xFF2196F3),
                unfocusedBorderColor = if (isDarkMode) Color(0xFF333333) else Color(0xFFF0F0F0),
                unfocusedContainerColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFF9F9F9),
                focusedContainerColor = if (isDarkMode) Color(0xFF121212) else Color(0xFFF9F9F9)
            )
        )
    }
}
