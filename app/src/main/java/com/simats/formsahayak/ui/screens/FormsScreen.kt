package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.formsahayak.ui.components.BottomNavigationBar
import com.simats.formsahayak.ui.viewmodel.FormViewModel
import com.simats.formsahayak.ui.viewmodel.RecentScan

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
    onProfileClick: () -> Unit
) {
    val context = LocalContext.current
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

    // Translation logic
    val title = when(selectedLanguage?.code) {
        "te" -> "నా ఫారమ్‌లు"
        "ta" -> "எனது படிவங்கள்"
        else -> "My Forms"
    }
    val searchPlaceholder = when(selectedLanguage?.code) {
        "te" -> "ఫారమ్‌ల కోసం వెతకండి..."
        "ta" -> "படிவங்களைத் தேடுங்கள்..."
        else -> "Search forms..."
    }
    val recentScansTitle = when(selectedLanguage?.code) {
        "te" -> "ఇటీవలి స్కాన్‌లు"
        "ta" -> "சமீபத்திய ஸ்கேன்கள்"
        else -> "Recent Scans"
    }

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = { Text(title, fontWeight = FontWeight.ExtraBold, fontSize = 22.sp, color = textColor) },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(containerColor = if (isHighContrast || isDarkMode) Color.Black else Color.White)
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
                onHelpClick = onHelpClick,
                onSettingsClick = onSettingsClick,
                onProfileClick = onProfileClick
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
                placeholder = { Text(searchPlaceholder, color = if (isHighContrast) Color.White else Color.Gray) },
                leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = if (isHighContrast) Color.Yellow else Color.Gray) },
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedTextColor = textColor,
                    unfocusedTextColor = textColor,
                    focusedContainerColor = cardColor,
                    unfocusedContainerColor = cardColor,
                    focusedBorderColor = if (isHighContrast) Color.Yellow else MaterialTheme.colorScheme.primary,
                    unfocusedBorderColor = if (isHighContrast) Color.White else Color.LightGray
                )
            )
            
            Spacer(modifier = Modifier.height(24.dp))
            
            Text(
                recentScansTitle,
                fontSize = 18.sp, 
                fontWeight = FontWeight.Bold, 
                color = textColor
            )
            
            Spacer(modifier = Modifier.height(16.dp))
            
            if (viewModel.recentScans.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        text = if (selectedLanguage?.code == "te") "స్కాన్‌లు ఏవీ లేవు" else "No recent scans",
                        color = if (isHighContrast) Color.White else Color.Gray
                    )
                }
            } else {
                LazyColumn(verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    items(viewModel.recentScans) { scan ->
                        RecentScanItem(
                            scan = scan,
                            textColor = textColor,
                            cardColor = cardColor,
                            isHighContrast = isHighContrast,
                            onClick = {
                                viewModel.selectScan(scan)
                                onScanSelected()
                            }
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun RecentScanItem(
    scan: RecentScan,
    textColor: Color,
    cardColor: Color,
    isHighContrast: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        color = cardColor,
        shadowElevation = if (isHighContrast) 0.dp else 2.dp,
        border = if (isHighContrast) BorderStroke(2.dp, Color.Yellow) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(Icons.Default.Description, contentDescription = null, tint = if (isHighContrast) Color.Yellow else Color(0xFF2196F3))
            Spacer(modifier = Modifier.width(16.dp))
            Text(scan.name, fontWeight = FontWeight.Bold, color = textColor)
        }
    }
}
