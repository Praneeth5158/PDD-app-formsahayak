package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.key.*
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay

@Composable
fun OtpResentScreen(
    userInput: String,
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onVerifyClick: (String) -> Unit,
    onBackToVerification: () -> Unit,
    onResendOtp: () -> Unit
) {
    val otpValues = remember { mutableStateListOf("", "", "", "", "", "") }
    val focusRequesters = remember { List(6) { FocusRequester() } }
    
    // Timer Logic - Starts at 45 seconds
    var timeLeft by remember { mutableIntStateOf(45) }
    LaunchedEffect(key1 = timeLeft) {
        if (timeLeft > 0) {
            delay(1000L)
            timeLeft--
        }
    }

    val isDark = isDarkMode || isHighContrast
    val backgroundColor = if (isDark) Color.Black else Color(0xFFF8FBFF)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundColor)
            .verticalScroll(rememberScrollState())
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(40.dp))

        // Top Icon - Purple Send Icon
        Surface(
            modifier = Modifier.size(80.dp),
            shape = CircleShape,
            color = Color(0xFF6200EE)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.Send,
                    contentDescription = null,
                    tint = Color.White,
                    modifier = Modifier.size(40.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "OTP Resent",
            fontSize = 28.sp,
            fontWeight = FontWeight.Bold,
            color = if (isDark) Color.White else Color(0xFF1A237E)
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "A new 6-digit code has been sent to\nyour phone",
            fontSize = 14.sp,
            color = if (isDark) Color.LightGray else Color.Gray,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Success Badge
        Surface(
            shape = RoundedCornerShape(20.dp),
            color = Color(0xFFE8F5E9),
            border = androidx.compose.foundation.BorderStroke(1.dp, Color(0xFFC8E6C9))
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(Icons.Default.CheckCircle, contentDescription = null, tint = Color(0xFF4CAF50), modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(8.dp))
                Text("New OTP Sent Successfully", fontSize = 12.sp, color = Color(0xFF2E7D32), fontWeight = FontWeight.Medium)
            }
        }

        Spacer(modifier = Modifier.height(32.dp))

        Surface(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(24.dp),
            color = cardColor,
            shadowElevation = 4.dp,
            border = if (isHighContrast) androidx.compose.foundation.BorderStroke(2.dp, Color.White) else null
        ) {
            Column(
                modifier = Modifier.padding(24.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Timer Banner
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    color = Color(0xFFE3F2FD)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(Icons.Default.AccessTime, contentDescription = null, tint = Color(0xFF2196F3), modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(8.dp))
                        Text("OTP expires in: $timeLeft seconds", fontSize = 13.sp, color = Color(0xFF1976D2), fontWeight = FontWeight.Bold)
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Text("Code sent to", fontSize = 12.sp, color = Color.Gray)
                Text(
                    text = maskInput(userInput),
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    color = textColor
                )

                Spacer(modifier = Modifier.height(24.dp))

                // 6 boxes for OTP
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    repeat(6) { index ->
                        Surface(
                            modifier = Modifier
                                .weight(1f)
                                .aspectRatio(0.8f),
                            shape = RoundedCornerShape(8.dp),
                            color = if (isDark) Color(0xFF121212) else Color(0xFFF5F5F5),
                            border = androidx.compose.foundation.BorderStroke(
                                width = if (otpValues[index].isNotEmpty()) 2.dp else 1.dp,
                                color = if (otpValues[index].isNotEmpty()) Color(0xFF2196F3) else if (isDark) Color(0xFF444444) else Color(0xFFE0E0E0)
                            )
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                BasicTextField(
                                    value = otpValues[index],
                                    onValueChange = { newValue ->
                                        val filtered = newValue.filter { it.isDigit() }
                                        val digit = filtered.takeLast(1)
                                        otpValues[index] = digit
                                        if (digit.isNotEmpty() && index < 5) {
                                            focusRequesters[index + 1].requestFocus()
                                        }
                                    },
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .focusRequester(focusRequesters[index])
                                        .onKeyEvent {
                                            if (it.key == Key.Backspace && it.type == KeyEventType.KeyDown && otpValues[index].isEmpty() && index > 0) {
                                                focusRequesters[index - 1].requestFocus()
                                                true
                                            } else false
                                        },
                                    textStyle = TextStyle(
                                        textAlign = TextAlign.Center,
                                        fontSize = 28.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    ),
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                                    singleLine = true,
                                    cursorBrush = SolidColor(textColor),
                                    decorationBox = { innerTextField ->
                                        Box(contentAlignment = Alignment.Center) {
                                            innerTextField()
                                        }
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(8.dp),
                    color = Color(0xFFFFFDE7)
                ) {
                    Text(
                        "Please enter the new OTP you\njust received",
                        modifier = Modifier.padding(12.dp),
                        textAlign = TextAlign.Center,
                        fontSize = 13.sp,
                        color = Color(0xFFF57F17),
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = { 
                        if (otpValues.all { it.isNotEmpty() }) {
                            onVerifyClick(otpValues.joinToString(""))
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF9C27B0))
                ) {
                    Text("Verify New OTP", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.height(20.dp))

                Text("Still didn't receive code?", fontSize = 13.sp, color = Color.Gray)
                Text(
                    text = if (timeLeft > 0) "You can resend OTP in $timeLeft seconds" else "Resend OTP now",
                    fontSize = 11.sp, 
                    color = if (timeLeft > 0) Color.LightGray else Color(0xFF2196F3),
                    modifier = Modifier.clickable(enabled = timeLeft == 0) { 
                        timeLeft = 45 // Reset timer if resending
                        onResendOtp()
                    }
                )

                Spacer(modifier = Modifier.height(24.dp))

                OutlinedButton(
                    onClick = onBackToVerification,
                    modifier = Modifier.fillMaxWidth().height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    border = androidx.compose.foundation.BorderStroke(1.dp, if (isDark) Color.White else Color.LightGray)
                ) {
                    Text("Back to Verification", color = if (isDark) Color.White else Color.Gray)
                }
            }
        }
    }
}

private fun maskInput(input: String): String {
    if (input.isEmpty()) return ""
    return if (input.contains("@")) {
        val parts = input.split("@")
        val name = parts[0]
        val domain = parts[1]
        if (name.length > 3) {
            name.take(3) + "****@" + domain
        } else {
            "****@" + domain
        }
    } else {
        if (input.length >= 10) {
            "+91 ***** " + input.takeLast(3)
        } else if (input.length >= 3) {
            "***** " + input.takeLast(3)
        } else {
            "*****"
        }
    }
}
