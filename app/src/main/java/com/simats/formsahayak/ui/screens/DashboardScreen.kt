package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.formsahayak.R
import com.simats.formsahayak.ui.components.BottomNavigationBar

@Composable
fun DashboardScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onChangeLanguage: () -> Unit,
    onNavigateToHowToUse: () -> Unit,
    onNavigateToForms: () -> Unit,
    onNavigateToSettings: () -> Unit,
    onNavigateToProfile: () -> Unit,
    onUploadClick: () -> Unit
) {
    val backgroundColor = when {
        isHighContrast -> Color.Black
        isDarkMode -> Color(0xFF121212)
        else -> Color(0xFFF8FBFF)
    }
    val textColor = if (isHighContrast || isDarkMode) Color.White else Color.Black
    val cardColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF1E1E1E) else Color.White

    Scaffold(
        bottomBar = { 
            BottomNavigationBar(
                currentScreen = "home",
                isDarkMode = isDarkMode,
                isHighContrast = isHighContrast,
                selectedLanguage = selectedLanguage,
                onHomeClick = { },
                onFormsClick = onNavigateToForms,
                onHelpClick = onNavigateToHowToUse,
                onSettingsClick = onNavigateToSettings,
                onProfileClick = onNavigateToProfile
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
        ) {
            HeaderSection(isDarkMode, isHighContrast, textColor, selectedLanguage)
            Spacer(modifier = Modifier.height(16.dp))
            LanguageSelectorRow(
                languageName = selectedLanguage?.name ?: "English",
                onChangeClick = onChangeLanguage,
                isDarkMode = isDarkMode,
                isHighContrast = isHighContrast,
                cardColor = cardColor,
                textColor = textColor,
                selectedLanguage = selectedLanguage
            )
            Spacer(modifier = Modifier.height(16.dp))
            WelcomeBanner(selectedLanguage, isHighContrast)
            Spacer(modifier = Modifier.height(24.dp))
            UploadFormCard(onUploadClick, isDarkMode, isHighContrast, cardColor, textColor, selectedLanguage)
            Spacer(modifier = Modifier.height(24.dp))
            QuickActionsRow(onNavigateToHowToUse, onNavigateToForms, onNavigateToSettings, isDarkMode, isHighContrast, textColor, selectedLanguage)
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun HeaderSection(isDarkMode: Boolean, isHighContrast: Boolean, textColor: Color, selectedLanguage: Language?) {
    Row(
        modifier = Modifier.fillMaxWidth().padding(16.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Surface(
                modifier = Modifier.size(40.dp), 
                shape = RoundedCornerShape(10.dp), 
                color = if (isHighContrast) Color.Black else Color(0xFF00ACC1),
                border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = Color.White, modifier = Modifier.size(20.dp))
                }
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(text = stringResource(R.string.app_name), fontSize = 16.sp, fontWeight = FontWeight.ExtraBold, color = if (isHighContrast) Color.White else if (isDarkMode) Color.White else Color(0xFF2196F3))
                Text(text = stringResource(R.string.welcome_back), fontSize = 10.sp, fontWeight = if (isHighContrast) FontWeight.Bold else FontWeight.Normal, color = if (isHighContrast) Color.White else if (isDarkMode) Color.LightGray else Color.Gray)
            }
        }
        Icon(imageVector = Icons.AutoMirrored.Filled.VolumeUp, contentDescription = null, tint = if (isHighContrast || isDarkMode) Color.White else Color(0xFF2196F3), modifier = Modifier.size(22.dp))
    }
}

@Composable
fun LanguageSelectorRow(languageName: String, onChangeClick: () -> Unit, isDarkMode: Boolean, isHighContrast: Boolean, cardColor: Color, textColor: Color, selectedLanguage: Language?) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp),
        shape = RoundedCornerShape(20.dp),
        color = cardColor,
        shadowElevation = if (isHighContrast) 0.dp else 2.dp,
        border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
    ) {
        Row(modifier = Modifier.padding(10.dp), verticalAlignment = Alignment.CenterVertically, horizontalArrangement = Arrangement.SpaceBetween) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(32.dp), 
                    shape = RoundedCornerShape(8.dp), 
                    color = if (isHighContrast) Color.Black else Color(0xFFF3E5F5),
                    border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(imageVector = Icons.Default.Translate, contentDescription = null, tint = if (isHighContrast) Color.White else Color(0xFF9C27B0), modifier = Modifier.size(16.dp))
                    }
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column {
                    Text(text = stringResource(R.string.language), fontSize = 10.sp, fontWeight = if (isHighContrast) FontWeight.Bold else FontWeight.Normal, color = if (isHighContrast) Color.White else if (isDarkMode) Color.LightGray else Color.Gray)
                    Text(text = languageName, fontSize = 13.sp, fontWeight = FontWeight.Bold, color = textColor)
                }
            }
            Button(
                onClick = onChangeClick, 
                colors = ButtonDefaults.buttonColors(containerColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF333333) else Color(0xFFE3F2FD)), 
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 0.dp), 
                modifier = Modifier.height(28.dp).then(if (isHighContrast) Modifier.border(BorderStroke(2.dp, Color.White), RoundedCornerShape(10.dp)) else Modifier)
            ) {
                Text(text = stringResource(R.string.change), color = if (isHighContrast) Color.White else Color(0xFF2196F3), fontSize = 10.sp, fontWeight = if (isHighContrast) FontWeight.ExtraBold else FontWeight.Normal)
            }
        }
    }
}

@Composable
fun WelcomeBanner(selectedLanguage: Language?, isHighContrast: Boolean) {
    val gradient = Brush.horizontalGradient(colors = listOf(Color(0xFF2196F3), Color(0xFF00E676)))
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .height(80.dp)
            .background(
                if (isHighContrast) Brush.linearGradient(listOf(Color.Black, Color.Black)) else gradient, 
                RoundedCornerShape(16.dp)
            )
            .then(if (isHighContrast) Modifier.border(BorderStroke(2.dp, Color.White), RoundedCornerShape(16.dp)) else Modifier)
            .padding(16.dp), 
        contentAlignment = Alignment.CenterStart
    ) {
        Text(text = stringResource(R.string.welcome_to_fs), color = Color.White, fontSize = 15.sp, fontWeight = FontWeight.Bold)
    }
}

@Composable
fun UploadFormCard(onUploadClick: () -> Unit, isDarkMode: Boolean, isHighContrast: Boolean, cardColor: Color, textColor: Color, selectedLanguage: Language?) {
    Surface(
        modifier = Modifier.fillMaxWidth().padding(horizontal = 20.dp).clickable { onUploadClick() },
        shape = RoundedCornerShape(24.dp),
        color = cardColor,
        shadowElevation = if (isHighContrast) 0.dp else 4.dp,
        border = if (isHighContrast) BorderStroke(3.dp, Color.White) else null
    ) {
        Column(modifier = Modifier.padding(20.dp), horizontalAlignment = Alignment.CenterHorizontally) {
            Surface(
                modifier = Modifier.size(100.dp), 
                shape = CircleShape, 
                color = if (isHighContrast) Color.Black else Color(0xFFE3F2FD),
                border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = if (isHighContrast) Color.White else Color(0xFF2196F3), modifier = Modifier.size(50.dp))
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text(text = stringResource(R.string.upload_form), fontSize = 20.sp, fontWeight = FontWeight.ExtraBold, color = textColor)
            Spacer(modifier = Modifier.height(24.dp))
            Button(
                onClick = onUploadClick, 
                modifier = Modifier.fillMaxWidth().height(48.dp).then(if (isHighContrast) Modifier.border(BorderStroke(2.dp, Color.White), RoundedCornerShape(10.dp)) else Modifier), 
                shape = RoundedCornerShape(10.dp), 
                colors = ButtonDefaults.buttonColors(containerColor = if (isHighContrast) Color.Black else Color(0xFF2196F3))
            ) {
                Text(stringResource(R.string.get_started), fontSize = 14.sp, fontWeight = FontWeight.Bold, color = Color.White)
            }
        }
    }
}

@Composable
fun QuickActionsRow(onHelpClick: () -> Unit, onFormsClick: () -> Unit, onSettingsClick: () -> Unit, isDarkMode: Boolean, isHighContrast: Boolean, textColor: Color, selectedLanguage: Language?) {
    Row(modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp), horizontalArrangement = Arrangement.SpaceEvenly) {
        QuickActionItem(stringResource(R.string.help), Icons.Default.HeadsetMic, Color(0xFFFFE0B2), Color(0xFFFB8C00), isDarkMode, isHighContrast, onHelpClick)
        QuickActionItem(stringResource(R.string.forms), Icons.Default.Description, Color(0xFFE8F5E9), Color(0xFF4CAF50), isDarkMode, isHighContrast, onFormsClick)
        QuickActionItem(stringResource(R.string.settings), Icons.Default.Translate, Color(0xFFF3E5F5), Color(0xFF9C27B0), isDarkMode, isHighContrast, onSettingsClick)
    }
}

@Composable
fun QuickActionItem(label: String, icon: ImageVector, bgColor: Color, iconColor: Color, isDarkMode: Boolean, isHighContrast: Boolean, onClick: () -> Unit) {
    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.clickable { onClick() }) {
        Surface(
            modifier = Modifier.size(50.dp), 
            shape = RoundedCornerShape(12.dp), 
            color = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF333333) else bgColor,
            border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
        ) {
            Box(contentAlignment = Alignment.Center) { 
                Icon(imageVector = icon, contentDescription = label, tint = if (isHighContrast) Color.White else iconColor, modifier = Modifier.size(20.dp)) 
            }
        }
        Spacer(modifier = Modifier.height(4.dp))
        Text(text = label, fontSize = 10.sp, fontWeight = if (isHighContrast) FontWeight.ExtraBold else FontWeight.Normal, color = if (isHighContrast) Color.White else if (isDarkMode) Color.LightGray else Color.Gray)
    }
}
