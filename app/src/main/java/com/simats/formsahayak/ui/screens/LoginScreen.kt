package com.simats.formsahayak.ui.screens

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Description
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.simats.formsahayak.ui.viewmodel.FormViewModel

@Composable
fun LoginScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    viewModel: FormViewModel,
    onLoginSuccess: () -> Unit,
    onNavigateToSignup: () -> Unit,
    onForgotPassword: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }

    val context = LocalContext.current
    val isLoading = viewModel.isLoading
    val apiErrorMessage = viewModel.errorMessage

    val isDark = isDarkMode || isHighContrast
    val backgroundColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF121212) else Color(0xFFF0F7FF)
    val cardColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryTextColor = if (isDark) Color.LightGray else Color.Gray

    // Language Strings
    val welcomeStr = when (selectedLanguage?.code) {
        "te" -> "స్వాగతం"
        "ta" -> "வரவேற்கிறோம்"
        else -> "Welcome Back"
    }
    val loginSubStr = when (selectedLanguage?.code) {
        "te" -> "కొనసాగడానికి లాగిన్ చేయండి"
        "ta" -> "தொடர உள்நுழையவும்"
        else -> "Login to continue"
    }
    val emailStr = when (selectedLanguage?.code) {
        "te" -> "ఈమెయిల్"
        "ta" -> "மின்னஞ்சல்"
        else -> "Email"
    }
    val passwordStr = when (selectedLanguage?.code) {
        "te" -> "పాస్‌వర్డ్"
        "ta" -> "கடவுச்சொல்"
        else -> "Password"
    }
    val forgotPwdStr = when (selectedLanguage?.code) {
        "te" -> "పాస్‌వర్డ్ మర్చిపోయారా?"
        "ta" -> "கடவுச்சொல்லை மறந்துவிட்டீர்களா?"
        else -> "Forgot Password?"
    }
    val loginBtnStr = when (selectedLanguage?.code) {
        "te" -> "లాగిన్"
        "ta" -> "உள்நுழை"
        else -> "Login"
    }
    val noAccountStr = when (selectedLanguage?.code) {
        "te" -> "ఖాతా లేదా? "
        "ta" -> "கணக்கு இல்லையா? "
        else -> "Don't have an account? "
    }
    val signUpStr = when (selectedLanguage?.code) {
        "te" -> "సైన్ అప్"
        "ta" -> "பதிவுபெறு"
        else -> "Sign Up"
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(60.dp))

        Surface(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            color = if (isHighContrast) Color.Yellow else Color(0xFF2196F3)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.Default.Description,
                    contentDescription = null,
                    tint = if (isHighContrast) Color.Black else Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = welcomeStr,
            fontSize = 32.sp,
            fontWeight = FontWeight.ExtraBold,
            color = if (isHighContrast) Color.Yellow else if (isDarkMode) Color.White else Color(0xFF1A237E)
        )

        Text(
            text = loginSubStr,
            fontSize = 16.sp,
            color = secondaryTextColor
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
                if (apiErrorMessage != null) {
                    Text(
                        text = apiErrorMessage,
                        color = Color.Red,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 12.dp)
                    )
                }

                Text(
                    text = emailStr,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = email,
                    onValueChange = { email = it },
                    placeholder = { Text(emailStr, color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Email, contentDescription = null, tint = Color.LightGray) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
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
                    text = passwordStr,
                    fontWeight = FontWeight.ExtraBold,
                    fontSize = 14.sp,
                    color = textColor
                )
                Spacer(modifier = Modifier.height(8.dp))
                TextField(
                    value = password,
                    onValueChange = { password = it },
                    placeholder = { Text(passwordStr, color = Color.Gray) },
                    leadingIcon = { Icon(Icons.Default.Lock, contentDescription = null, tint = Color.LightGray) },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                                contentDescription = if (passwordVisible) "Hide password" else "Show password",
                                tint = Color.LightGray
                            )
                        }
                    },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading,
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

                Spacer(modifier = Modifier.height(12.dp))

                TextButton(
                    onClick = onForgotPassword,
                    enabled = !isLoading,
                    contentPadding = PaddingValues(0.dp)
                ) {
                    Text(
                        text = forgotPwdStr, 
                        color = if (isHighContrast) Color.Yellow else Color(0xFF2196F3), 
                        fontSize = 14.sp, 
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                Button(
                    onClick = { 
                        viewModel.login(email, password) { success, msg ->
                            if (success) {
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                                onLoginSuccess()
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    enabled = !isLoading,
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isHighContrast) Color.Yellow else Color(0xFF2196F3),
                        contentColor = if (isHighContrast) Color.Black else Color.White
                    )
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(color = if (isHighContrast) Color.Black else Color.White, modifier = Modifier.size(24.dp))
                    } else {
                        Text(loginBtnStr, fontSize = 16.sp, fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(noAccountStr, color = secondaryTextColor, fontSize = 14.sp)
                    TextButton(
                        onClick = onNavigateToSignup,
                        enabled = !isLoading,
                        contentPadding = PaddingValues(0.dp)
                    ) {
                        Text(
                            signUpStr, 
                            color = if (isHighContrast) Color.Yellow else Color(0xFF2196F3), 
                            fontWeight = FontWeight.ExtraBold, 
                            fontSize = 14.sp
                        )
                    }
                }
            }
        }
    }
}
