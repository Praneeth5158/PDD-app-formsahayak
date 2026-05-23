package com.simats.formsahayak.ui.components

import android.util.Log
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.HelpOutline
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.formsahayak.ui.screens.Language

@Composable
fun BottomNavigationBar(
    currentScreen: String,
    isDarkMode: Boolean = false,
    isHighContrast: Boolean = false,
    selectedLanguage: Language? = null,
    onHomeClick: () -> Unit,
    onFormsClick: () -> Unit,
    onHelpClick: () -> Unit,
    onSettingsClick: () -> Unit,
    onProfileClick: () -> Unit
) {
    val labels = if (selectedLanguage?.code == "te") {
        listOf("హోమ్", "ఫారమ్‌లు", "సహాయం", "సెట్టింగ్‌లు", "ప్రొఫైల్")
    } else if (selectedLanguage?.code == "ta") {
        listOf("முகப்பு", "படிவங்கள்", "உதவி", "அமைப்புகள்", "சுயவிவரம்")
    } else {
        listOf("Home", "Forms", "Help", "Settings", "Profile")
    }

    NavigationBar(
        containerColor = if (isHighContrast || isDarkMode) Color.Black else Color.White,
        tonalElevation = 8.dp,
        windowInsets = WindowInsets(0, 0, 0, 0),
        modifier = if (isHighContrast) Modifier.border(BorderStroke(1.dp, Color.White)) else Modifier
    ) {
        val activeColor = if (isHighContrast) Color.White else Color(0xFF2196F3)
        val inactiveColor = if (isHighContrast) Color.Gray else Color.Gray

        NavigationBarItem(
            icon = { Icon(if (currentScreen == "home") Icons.Default.Home else Icons.Outlined.Home, contentDescription = null, modifier = Modifier.size(22.dp)) },
            label = { Text(labels[0], fontSize = 10.sp, fontWeight = if (isHighContrast) FontWeight.Bold else FontWeight.Normal) },
            selected = currentScreen == "home",
            onClick = {
                Log.d("BottomNav", "Home clicked")
                onHomeClick()
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                selectedTextColor = activeColor,
                unselectedIconColor = inactiveColor,
                unselectedTextColor = inactiveColor,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(if (currentScreen == "forms") Icons.Default.Description else Icons.Outlined.Description, contentDescription = null, modifier = Modifier.size(22.dp)) },
            label = { Text(labels[1], fontSize = 10.sp, fontWeight = if (isHighContrast) FontWeight.Bold else FontWeight.Normal) },
            selected = currentScreen == "forms",
            onClick = {
                Log.d("BottomNav", "Forms clicked")
                onFormsClick()
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                selectedTextColor = activeColor,
                unselectedIconColor = inactiveColor,
                unselectedTextColor = inactiveColor,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(if (currentScreen == "help") Icons.Default.Help else Icons.AutoMirrored.Outlined.HelpOutline, contentDescription = null, modifier = Modifier.size(22.dp)) },
            label = { Text(labels[2], fontSize = 10.sp, fontWeight = if (isHighContrast) FontWeight.Bold else FontWeight.Normal) },
            selected = currentScreen == "help",
            onClick = {
                Log.d("BottomNav", "Help clicked")
                onHelpClick()
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                selectedTextColor = activeColor,
                unselectedIconColor = inactiveColor,
                unselectedTextColor = inactiveColor,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(if (currentScreen == "settings") Icons.Default.Settings else Icons.Outlined.Settings, contentDescription = null, modifier = Modifier.size(22.dp)) },
            label = { Text(labels[3], fontSize = 10.sp, fontWeight = if (isHighContrast) FontWeight.Bold else FontWeight.Normal) },
            selected = currentScreen == "settings",
            onClick = {
                Log.d("BottomNav", "Settings clicked")
                onSettingsClick()
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                selectedTextColor = activeColor,
                unselectedIconColor = inactiveColor,
                unselectedTextColor = inactiveColor,
                indicatorColor = Color.Transparent
            )
        )
        NavigationBarItem(
            icon = { Icon(if (currentScreen == "profile") Icons.Default.Person else Icons.Outlined.Person, contentDescription = null, modifier = Modifier.size(22.dp)) },
            label = { Text(labels[4], fontSize = 10.sp, fontWeight = if (isHighContrast) FontWeight.Bold else FontWeight.Normal) },
            selected = currentScreen == "profile",
            onClick = {
                Log.d("BottomNav", "Profile clicked")
                onProfileClick()
            },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = activeColor,
                selectedTextColor = activeColor,
                unselectedIconColor = inactiveColor,
                unselectedTextColor = inactiveColor,
                indicatorColor = Color.Transparent
            )
        )
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String, iconColor: Color, isDarkMode: Boolean = false, isHighContrast: Boolean = false) {
    val displayIconColor = if (isHighContrast) Color.White else iconColor
    val labelTextColor = if (isHighContrast) Color.White else Color.Gray
    val valueTextColor = if (isHighContrast || isDarkMode) Color.White else Color.Black

    Row(verticalAlignment = Alignment.CenterVertically) {
        Surface(
            modifier = Modifier.size(36.dp),
            shape = RoundedCornerShape(8.dp),
            color = displayIconColor.copy(alpha = 0.1f),
            border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(imageVector = icon, contentDescription = null, tint = displayIconColor, modifier = Modifier.size(20.dp))
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column {
            Text(text = label, fontSize = 11.sp, color = labelTextColor)
            Text(text = value, fontSize = 14.sp, fontWeight = if (isHighContrast) FontWeight.Bold else FontWeight.Medium, color = valueTextColor)
        }
    }
}

@Composable
fun ProfileActionItem(icon: ImageVector, label: String, color: Color = Color.Black, isDarkMode: Boolean = false, isHighContrast: Boolean = false, onClick: () -> Unit) {
    val textColor = when {
        color == Color.Red -> color
        isHighContrast || isDarkMode -> Color.White
        else -> Color.Black
    }
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(imageVector = icon, contentDescription = null, tint = textColor.copy(alpha = 0.7f), modifier = Modifier.size(20.dp))
        Spacer(modifier = Modifier.width(16.dp))
        Text(text = label, fontSize = 15.sp, fontWeight = if (isHighContrast) FontWeight.Bold else FontWeight.Medium, color = textColor)
        Spacer(modifier = Modifier.weight(1f))
        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = if (isHighContrast) Color.White else Color.LightGray)
    }
}
