package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.formsahayak.R
import com.simats.formsahayak.ui.components.BottomNavigationBar
import com.simats.formsahayak.ui.viewmodel.FormViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(
    currentLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    viewModel: FormViewModel,
    onThemeChange: (Boolean) -> Unit,
    onHighContrastChange: (Boolean) -> Unit,
    onLanguageChange: (Language) -> Unit,
    onHomeClick: () -> Unit,
    onFormsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onProfileClick: () -> Unit,
    onNavigateToVoiceSettings: (String) -> Unit
) {
    val backgroundColor = if (isHighContrast) Color.Black else (if (isDarkMode) Color(0xFF121212) else Color(0xFFF8FBFF))
    val cardColor = if (isHighContrast) Color.Black else (if (isDarkMode) Color(0xFF1E1E1E) else Color.White)
    val textColor = if (isDarkMode || isHighContrast) Color.White else Color(0xFF2C3E50)
    val subCardColor = if (isHighContrast) Color.Black else (if (isDarkMode) Color(0xFF121212) else Color(0xFFF1F4F9))
    val secondaryTextColor = if (isDarkMode || isHighContrast) Color.LightGray else Color.Gray

    Scaffold(
        topBar = {
            CenterAlignedTopAppBar(
                title = {
                    Text(
                        stringResource(R.string.settings),
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 20.sp,
                        color = textColor
                    )
                },
                colors = TopAppBarDefaults.centerAlignedTopAppBarColors(
                    containerColor = backgroundColor,
                    titleContentColor = textColor
                ),
                modifier = Modifier
                    .background(backgroundColor)
                    .then(if (isHighContrast) Modifier.border(0.5.dp, Color.White) else Modifier)
            )
        },
        bottomBar = {
            BottomNavigationBar(
                currentScreen = "settings",
                isDarkMode = isDarkMode,
                isHighContrast = isHighContrast,
                selectedLanguage = currentLanguage,
                onHomeClick = onHomeClick,
                onFormsClick = onFormsClick,
                onHelpClick = onHelpClick,
                onSettingsClick = { },
                onProfileClick = onProfileClick
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
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Language Section
            SettingsCard(
                title = stringResource(R.string.language),
                icon = Icons.Default.Translate,
                iconBgColor = if (isHighContrast) Color.White else (if (isDarkMode) Color(0xFF2C3E50) else Color(0xFFE8F0FE)),
                iconColor = if (isHighContrast) Color.Black else (if (isDarkMode) Color.White else Color(0xFF1A73E8)),
                cardColor = cardColor,
                textColor = textColor,
                isHighContrast = isHighContrast
            ) {
                Text(stringResource(R.string.select_language), color = textColor, fontSize = 14.sp, fontWeight = FontWeight.SemiBold)
                Spacer(modifier = Modifier.height(8.dp))
                
                var languageExpanded by remember { mutableStateOf(false) }
                val languages = listOf(
                    Language("English", "English", "en"),
                    Language("Telugu", "తెలుగు", "te"),
                    Language("Tamil", "தமிழ்", "ta"),
                    Language("Hindi", "हिन्दी", "hi")
                )
                
                DropdownSelector(
                    value = currentLanguage?.name ?: "English",
                    isExpanded = languageExpanded,
                    onToggle = { languageExpanded = !languageExpanded },
                    bgColor = subCardColor,
                    textColor = textColor,
                    isHighContrast = isHighContrast
                ) {
                    DropdownMenu(
                        expanded = languageExpanded,
                        onDismissRequest = { languageExpanded = false },
                        modifier = Modifier
                            .background(cardColor)
                            .then(if (isHighContrast) Modifier.border(1.dp, Color.White) else Modifier)
                    ) {
                        languages.forEach { lang ->
                            DropdownMenuItem(
                                text = { Text(lang.name, color = textColor) },
                                onClick = {
                                    onLanguageChange(lang)
                                    languageExpanded = false
                                }
                            )
                        }
                    }
                }
            }

            // Voice Guidance Section
            SettingsCard(
                title = stringResource(R.string.voice_guidance),
                icon = Icons.AutoMirrored.Filled.VolumeUp,
                iconBgColor = if (isHighContrast) Color.White else (if (isDarkMode) Color(0xFF1B5E20) else Color(0xFFE8F5E9)),
                iconColor = if (isHighContrast) Color.Black else (if (isDarkMode) Color.White else Color(0xFF2E7D32)),
                cardColor = cardColor,
                textColor = textColor,
                isHighContrast = isHighContrast
            ) {
                Surface(
                    color = subCardColor,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { onNavigateToVoiceSettings(viewModel.voiceSpeed) }
                        .then(if (isHighContrast) Modifier.border(1.dp, Color.White, RoundedCornerShape(12.dp)) else Modifier)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(stringResource(R.string.voice_speed), fontWeight = FontWeight.Bold, fontSize = 15.sp, color = textColor)
                            val speedText = when(viewModel.voiceSpeed) {
                                "Slow" -> stringResource(R.string.speed_slow)
                                "Normal" -> stringResource(R.string.speed_normal)
                                "Fast" -> stringResource(R.string.speed_fast)
                                else -> viewModel.voiceSpeed
                            }
                            Text(speedText, color = secondaryTextColor, fontSize = 12.sp)
                        }
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = null,
                            tint = if (textColor == Color.White) Color.White else Color.Gray
                        )
                    }
                }
            }

            // Accessibility Section
            SettingsCard(
                title = stringResource(R.string.accessibility),
                icon = Icons.Default.Visibility,
                iconBgColor = if (isHighContrast) Color.White else (if (isDarkMode) Color(0xFF4A148C) else Color(0xFFF3E5F5)),
                iconColor = if (isHighContrast) Color.Black else (if (isDarkMode) Color.White else Color(0xFF7B1FA2)),
                cardColor = cardColor,
                textColor = textColor,
                isHighContrast = isHighContrast
            ) {
                Surface(
                    color = subCardColor,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics(mergeDescendants = true) { }
                        .toggleable(
                            value = isHighContrast,
                            role = Role.Switch,
                            onValueChange = { onHighContrastChange(it) }
                        )
                        .then(if (isHighContrast) Modifier.border(1.dp, Color.White, RoundedCornerShape(12.dp)) else Modifier)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(stringResource(R.string.high_contrast), fontWeight = FontWeight.Bold, fontSize = 15.sp, color = textColor)
                            Text(stringResource(R.string.high_contrast_desc), color = secondaryTextColor, fontSize = 12.sp)
                        }
                        Switch(
                            checked = isHighContrast,
                            onCheckedChange = null,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = if (isHighContrast) Color.Yellow else Color(0xFF7B1FA2)
                            )
                        )
                    }
                }
            }

            // Display Section
            SettingsCard(
                title = stringResource(R.string.display),
                icon = Icons.Default.NightlightRound,
                iconBgColor = if (isHighContrast) Color.White else (if (isDarkMode) Color(0xFF1A237E) else Color(0xFFE8EAF6)),
                iconColor = if (isHighContrast) Color.Black else (if (isDarkMode) Color.White else Color(0xFF3949AB)),
                cardColor = cardColor,
                textColor = textColor,
                isHighContrast = isHighContrast
            ) {
                Surface(
                    color = subCardColor,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .semantics(mergeDescendants = true) { }
                        .toggleable(
                            value = isDarkMode,
                            role = Role.Switch,
                            onValueChange = { onThemeChange(it) }
                        )
                        .then(if (isHighContrast) Modifier.border(1.dp, Color.White, RoundedCornerShape(12.dp)) else Modifier)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(stringResource(R.string.dark_mode), fontWeight = FontWeight.Bold, fontSize = 15.sp, color = textColor)
                            Text(stringResource(R.string.dark_mode_desc), color = secondaryTextColor, fontSize = 12.sp)
                        }
                        Switch(
                            checked = isDarkMode,
                            onCheckedChange = null,
                            colors = SwitchDefaults.colors(
                                checkedThumbColor = Color.White,
                                checkedTrackColor = Color(0xFF3949AB)
                            )
                        )
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}

@Composable
fun SettingsCard(
    title: String,
    icon: ImageVector,
    iconBgColor: Color,
    iconColor: Color,
    cardColor: Color,
    textColor: Color,
    isHighContrast: Boolean = false,
    content: @Composable ColumnScope.() -> Unit
) {
    Surface(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(28.dp),
        color = cardColor,
        shadowElevation = if (isHighContrast) 0.dp else 0.5.dp,
        border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Surface(
                    modifier = Modifier.size(44.dp),
                    shape = CircleShape,
                    color = iconBgColor,
                    border = if (isHighContrast) BorderStroke(1.dp, Color.Black) else null
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Icon(icon, contentDescription = null, tint = iconColor, modifier = Modifier.size(24.dp))
                    }
                }
                Spacer(modifier = Modifier.width(16.dp))
                Text(title, fontSize = 18.sp, fontWeight = FontWeight.Bold, color = textColor)
            }
            Spacer(modifier = Modifier.height(16.dp))
            content()
        }
    }
}

@Composable
fun DropdownSelector(
    value: String,
    isExpanded: Boolean,
    onToggle: () -> Unit,
    bgColor: Color,
    textColor: Color,
    isHighContrast: Boolean = false,
    content: @Composable () -> Unit
) {
    Box {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(
                    role = Role.Button,
                    onClickLabel = if (isExpanded) "Close options" else "Show options"
                ) { onToggle() },
            color = bgColor,
            shape = RoundedCornerShape(12.dp),
            border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 14.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(value, fontSize = 15.sp, fontWeight = FontWeight.Medium, color = textColor)
                Icon(
                    imageVector = if (isExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.KeyboardArrowDown,
                    contentDescription = null,
                    tint = if (textColor == Color.White) Color.White else Color.Gray
                )
            }
        }
        content()
    }
}
