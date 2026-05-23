package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ResetPasswordScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    isChangePassword: Boolean = false,
    onResetPasswordClick: (String, String) -> Unit,
    onBackClick: () -> Unit
) {
    var newPassword by remember { mutableStateOf("") }
    var confirmPassword by remember { mutableStateOf("") }
    var newPasswordVisible by remember { mutableStateOf(false) }
    var confirmPasswordVisible by remember { mutableStateOf(false) }

    val isDark = isDarkMode || isHighContrast
    val backgroundColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF121212) else Color(0xFFF0F7FF)
    val cardColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    // Translation logic
    val title = when (selectedLanguage?.code) {
        "te" -> if (isChangePassword) "పాస్‌వర్డ్ మార్చండి" else "పాస్‌వర్డ్ రీసెట్ చేయండి"
        "ta" -> if (isChangePassword) "கடவுச்சொல்லை மாற்றவும்" else "கடவுச்சொல்லை மீட்டமைக்கவும்"
        "hi" -> if (isChangePassword) "पासवर्ड बदलें" else "Reset Password"
        else -> if (isChangePassword) "Change Password" else "Reset Password"
    }
    val subtitle = when (selectedLanguage?.code) {
        "te" -> "మీ ఖాతా కోసం కొత్త పాస్‌వర్డ్‌ను సృష్టించండి"
        "ta" -> "உங்கள் கணக்கிற்கு புதிய கடவுச்சொல்லை உருவாக்கவும்"
        "hi" -> "अपने खाते के लिए नया पासवर्ड बनाएँ"
        else -> "Create a new password for your account"
    }
    val newPasswordLabel = when (selectedLanguage?.code) {
        "te" -> "కొత్త పాస్‌వర్డ్"
        "ta" -> "புதிய கடவுச்சொல்"
        "hi" -> "नया पासवर्ड"
        else -> "New Password"
    }
    val newPasswordPlaceholder = when (selectedLanguage?.code) {
        "te" -> "కొత్త పాస్‌వర్డ్‌ను నమోదు చేయండి"
        "ta" -> "புதிய கடவுச்சொல்லை உள்ளிடவும்"
        "hi" -> "नया पासवर्ड दर्ज करें"
        else -> "Enter new password"
    }
    val confirmPasswordLabel = when (selectedLanguage?.code) {
        "te" -> "పాస్‌వర్డ్‌ను ధృవీకరించండి"
        "ta" -> "கடவுச்சொல்லை உறுதிப்படுத்தவும்"
        "hi" -> "पासवर्ड की पुष्टि करें"
        else -> "Confirm Password"
    }
    val confirmPasswordPlaceholder = when (selectedLanguage?.code) {
        "te" -> "కొత్త పాస్‌వర్డ్‌ను ధృవీకరించండి"
        "ta" -> "புதிய கடவுச்சொல்லை உறுதிப்படுத்தவும்"
        "hi" -> "नए पासवर्ड की पुष्टि करें"
        else -> "Confirm new password"
    }
    val resetButtonText = when (selectedLanguage?.code) {
        "te" -> if (isChangePassword) "పాస్‌వర్డ్ మార్చండి" else "పాస్‌వర్డ్ రీసెట్ చేయండి"
        "ta" -> if (isChangePassword) "கடவுச்சொல்லை மாற்றவும்" else "கடவுச்சொல்லை மீட்டமைக்கவும்"
        "hi" -> if (isChangePassword) "Update Password" else "Reset Password"
        else -> if (isChangePassword) "Update Password" else "Reset Password"
    }
    val backButtonText = when (selectedLanguage?.code) {
        "te" -> if (isChangePassword) "తిరిగి వెళ్ళండి" else "లాగిన్‌కి తిరిగి వెళ్లండి"
        "ta" -> if (isChangePassword) "மீண்டும் செல்" else "உள்நுழைவுக்குத் திரும்பு"
        "hi" -> if (isChangePassword) "Back to Profile" else "लॉगिन पर वापस जाएं"
        else -> if (isChangePassword) "Back to Profile" else "Back to Login"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(80.dp))

        Surface(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            color = if (isHighContrast) Color.Yellow else Color(0xFF00C853)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = if (isHighContrast) Color.Black else Color.White,
                    modifier = Modifier.size(45.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = title,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            color = if (isHighContrast) Color.Yellow else if (isDarkMode) Color.White else Color(0xFF1A237E)
        )

        Text(
            text = subtitle,
            fontSize = 14.sp,
            color = if (isDark) Color.LightGray else Color.Gray,
            modifier = Modifier.padding(top = 8.dp)
        )

        Spacer(modifier = Modifier.height(40.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = cardColor,
            shadowElevation = if (isHighContrast) 0.dp else 4.dp,
            border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
        ) {
            Column(
                modifier = Modifier.padding(24.dp)
            ) {
                Text(
                    text = newPasswordLabel,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = newPassword,
                    onValueChange = { newPassword = it },
                    placeholder = { Text(newPasswordPlaceholder, color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.LightGray) },
                    trailingIcon = {
                        IconButton(onClick = { newPasswordVisible = !newPasswordVisible }) {
                            Icon(
                                imageVector = if (newPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (newPasswordVisible) "Hide password" else "Show password",
                                tint = Color.LightGray
                            )
                        }
                    },
                    visualTransformation = if (newPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F5F5),
                        unfocusedContainerColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F5F5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(20.dp))

                Text(
                    text = confirmPasswordLabel,
                    fontWeight = FontWeight.Medium,
                    fontSize = 14.sp,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = confirmPassword,
                    onValueChange = { confirmPassword = it },
                    placeholder = { Text(confirmPasswordPlaceholder, color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.LightGray) },
                    trailingIcon = {
                        IconButton(onClick = { confirmPasswordVisible = !confirmPasswordVisible }) {
                            Icon(
                                imageVector = if (confirmPasswordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (confirmPasswordVisible) "Hide password" else "Show password",
                                tint = Color.LightGray
                            )
                        }
                    },
                    visualTransformation = if (confirmPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        focusedContainerColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F5F5),
                        unfocusedContainerColor = if (isDark) Color(0xFF121212) else Color(0xFFF5F5F5),
                        focusedIndicatorColor = Color.Transparent,
                        unfocusedIndicatorColor = Color.Transparent,
                        focusedTextColor = textColor,
                        unfocusedTextColor = textColor
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(32.dp))

                Button(
                    onClick = { onResetPasswordClick(newPassword, confirmPassword) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isHighContrast) Color.Yellow else Color(0xFF00C853),
                        contentColor = if (isHighContrast) Color.Black else Color.White
                    )
                ) {
                    Text(resetButtonText, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }
            }
        }
        
        Spacer(modifier = Modifier.height(24.dp))
        
        TextButton(onClick = onBackClick) {
            Text(backButtonText, color = if (isHighContrast) Color.White else Color(0xFF2196F3))
        }
    }
}
