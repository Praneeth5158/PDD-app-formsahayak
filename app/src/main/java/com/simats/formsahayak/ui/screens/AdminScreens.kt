package com.simats.formsahayak.ui.screens

import android.graphics.Paint
import android.widget.Toast
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.simats.formsahayak.R
import com.simats.formsahayak.logic.*
import com.simats.formsahayak.ui.viewmodel.FormViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import android.graphics.Bitmap
import android.graphics.ImageDecoder
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.ui.graphics.asImageBitmap
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.ByteArrayOutputStream
import java.io.FileOutputStream
import java.io.File
import android.os.Build
import androidx.compose.foundation.Image

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminLoginScreen(
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onLoginSuccess: () -> Unit,
    onBackClick: () -> Unit
) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var isLoading by remember { mutableStateOf(false) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val context = LocalContext.current
    val isDark = isDarkMode || isHighContrast
    val backgroundColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF121212) else Color(0xFFF0F7FF)
    val cardColor = if (isHighContrast) Color.Black else if (isDarkMode) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryTextColor = if (isDark) Color.LightGray else Color.Gray

    val scope = rememberCoroutineScope()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Portal", fontWeight = FontWeight.ExtraBold, fontSize = 20.sp, color = textColor) },
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
            Spacer(modifier = Modifier.height(40.dp))

            Surface(
                modifier = Modifier.size(80.dp),
                shape = CircleShape,
                color = if (isHighContrast) Color.Yellow else Color(0xFFE53935)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(
                        imageVector = Icons.Default.AdminPanelSettings,
                        contentDescription = null,
                        tint = if (isHighContrast) Color.Black else Color.White,
                        modifier = Modifier.size(45.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Welcome Admin",
                fontSize = 30.sp,
                fontWeight = FontWeight.ExtraBold,
                color = if (isHighContrast) Color.Yellow else if (isDarkMode) Color.White else Color(0xFFC62828)
            )

            Text(
                text = "Log in with your administrator credentials",
                fontSize = 14.sp,
                color = secondaryTextColor,
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(40.dp))

            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = cardColor,
                shadowElevation = if (isHighContrast) 0.dp else 4.dp,
                border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
            ) {
                Column(
                    modifier = Modifier.padding(24.dp)
                ) {
                    if (errorMsg != null) {
                        Text(
                            text = errorMsg!!,
                            color = Color.Red,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    Text(
                        text = "Admin Email",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = email,
                        onValueChange = { email = it },
                        placeholder = { Text("Enter admin email", color = Color.Gray) },
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
                        text = "Password",
                        fontWeight = FontWeight.ExtraBold,
                        fontSize = 14.sp,
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    TextField(
                        value = password,
                        onValueChange = { password = it },
                        placeholder = { Text("Enter password", color = Color.Gray) },
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

                    Spacer(modifier = Modifier.height(32.dp))

                    Button(
                        onClick = {
                            if (email.isBlank() || password.isBlank()) {
                                errorMsg = "Email and Password cannot be empty"
                                return@Button
                            }
                            isLoading = true
                            errorMsg = null
                            scope.launch {
                                try {
                                    val req = AdminLoginRequest(email.trim(), password.trim())
                                    val res = adminApiService.login(req)
                                    if (res.isSuccessful) {
                                        val token = res.body()?.accessToken
                                        if (token != null) {
                                            SecureStore.saveToken(context, token)
                                            Toast.makeText(context, "Admin Authentication Successful", Toast.LENGTH_SHORT).show()
                                            onLoginSuccess()
                                        } else {
                                            errorMsg = "Login response was empty"
                                        }
                                    } else {
                                        errorMsg = "Invalid Admin Credentials"
                                    }
                                } catch (e: Exception) {
                                    errorMsg = "Connection Error: ${e.localizedMessage}"
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(56.dp),
                        enabled = !isLoading,
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isHighContrast) Color.Yellow else Color(0xFFE53935),
                            contentColor = if (isHighContrast) Color.Black else Color.White
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(color = if (isHighContrast) Color.Black else Color.White, modifier = Modifier.size(24.dp))
                        } else {
                            Text("Login as Admin", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminDashboardScreen(
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    viewModel: FormViewModel,
    onLogoutClick: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Overview", "Users", "Form Logs", "Feedback", "Developer")

    val isDark = isDarkMode || isHighContrast
    val backgroundColor = if (isDark) Color.Black else Color(0xFFF8FBFF)
    val textColor = if (isDark) Color.White else Color.Black

    Scaffold(
        topBar = {
            Surface(
                color = if (isDark) Color.Black else Color.White,
                shadowElevation = 4.dp
            ) {
                Column {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .statusBarsPadding()
                            .padding(horizontal = 16.dp, vertical = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Text(
                            text = "Admin Dashboard",
                            fontWeight = FontWeight.ExtraBold,
                            fontSize = 22.sp,
                            color = textColor
                        )
                        IconButton(onClick = onLogoutClick) {
                            Icon(imageVector = Icons.Default.ExitToApp, contentDescription = "Logout", tint = Color.Red)
                        }
                    }
                    ScrollableTabRow(
                        selectedTabIndex = selectedTab,
                        containerColor = if (isDark) Color.Black else Color.White,
                        contentColor = if (isHighContrast) Color.White else Color(0xFF2196F3),
                        edgePadding = 16.dp
                    ) {
                        tabs.forEachIndexed { index, title ->
                            Tab(
                                selected = selectedTab == index,
                                onClick = { selectedTab = index },
                                text = { Text(title, fontSize = 14.sp, fontWeight = FontWeight.Bold) }
                            )
                        }
                    }
                }
            }
        },
        containerColor = backgroundColor
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .background(backgroundColor)
        ) {
            when (selectedTab) {
                0 -> AdminOverviewTab(isDarkMode, isHighContrast)
                1 -> AdminUsersTab(isDarkMode, isHighContrast)
                2 -> AdminFormsTab(isDarkMode, isHighContrast, viewModel)
                3 -> AdminFeedbackTab(isDarkMode, isHighContrast)
                4 -> AdminDeveloperTab(isDarkMode, isHighContrast)
            }
        }
    }
}

@Composable
fun AdminOverviewTab(isDarkMode: Boolean, isHighContrast: Boolean) {
    val context = LocalContext.current
    var stats by remember { mutableStateOf<AdminStatsResponse?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val isDark = isDarkMode || isHighContrast
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryTextColor = if (isDark) Color.LightGray else Color.Gray
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White

    LaunchedEffect(Unit) {
        val token = SecureStore.getToken(context)
        if (token == null) {
            errorMsg = "Unauthorized: No admin token found"
            isLoading = false
            return@LaunchedEffect
        }
        try {
            val res = adminApiService.getStats("Bearer $token")
            if (res.isSuccessful) {
                stats = res.body()
            } else {
                errorMsg = "Failed to load stats: ${res.message()}"
            }
        } catch (e: Exception) {
            errorMsg = "Error: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = if (isHighContrast) Color.White else Color(0xFF2196F3))
        }
    } else if (errorMsg != null) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
            Text(errorMsg!!, color = Color.Red, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        }
    } else if (stats != null) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Stats grid cards
            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    StatCard("Total Users", stats!!.totalUsers.toString(), Icons.Default.People, Color(0xFF2196F3), cardColor, textColor, isHighContrast)
                }
                Box(modifier = Modifier.weight(1f)) {
                    StatCard("Active Users", stats!!.activeUsers.toString(), Icons.Default.Person, Color(0xFF4CAF50), cardColor, textColor, isHighContrast)
                }
            }

            Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                Box(modifier = Modifier.weight(1f)) {
                    StatCard("Uploaded Forms", stats!!.totalUploadedForms.toString(), Icons.Default.Description, Color(0xFFFF9800), cardColor, textColor, isHighContrast)
                }
                Box(modifier = Modifier.weight(1f)) {
                    StatCard("OCR Scans", stats!!.totalOcrScans.toString(), Icons.Default.QrCodeScanner, Color(0xFF9C27B0), cardColor, textColor, isHighContrast)
                }
            }

            StatCard("Total Feedbacks", stats!!.totalFeedback.toString(), Icons.Default.Feedback, Color(0xFFE91E63), cardColor, textColor, isHighContrast, modifier = Modifier.fillMaxWidth())

            Spacer(modifier = Modifier.height(8.dp))

            // Chart Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = cardColor,
                shadowElevation = if (isHighContrast) 0.dp else 4.dp,
                border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = "Daily Upload Trend",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = textColor
                    )
                    Text(
                        text = "Trend analysis of forms processed daily",
                        fontSize = 12.sp,
                        color = secondaryTextColor
                    )
                    Spacer(modifier = Modifier.height(20.dp))

                    val dates = stats!!.analytics?.uploadDates ?: emptyList()
                    val counts = stats!!.analytics?.uploadCounts ?: emptyList()
                    val trendPoints = dates.zip(counts).map { (date, count) -> Pair(date, count.toFloat()) }
                    if (trendPoints.isNotEmpty()) {
                        LineChart(
                            dataPoints = trendPoints,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            lineColor = if (isHighContrast) Color.White else Color(0xFF2196F3),
                            textColor = textColor
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text("No trend data available", color = secondaryTextColor, fontSize = 14.sp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun StatCard(
    title: String,
    value: String,
    icon: ImageVector,
    iconColor: Color,
    cardColor: Color,
    textColor: Color,
    isHighContrast: Boolean,
    modifier: Modifier = Modifier
) {
    Surface(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        color = cardColor,
        shadowElevation = if (isHighContrast) 0.dp else 2.dp,
        border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Surface(
                modifier = Modifier.size(44.dp),
                shape = CircleShape,
                color = iconColor.copy(alpha = 0.15f)
            ) {
                Box(contentAlignment = Alignment.Center) {
                    Icon(imageVector = icon, contentDescription = null, tint = if (isHighContrast) Color.White else iconColor, modifier = Modifier.size(24.dp))
                }
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column {
                Text(text = title, fontSize = 12.sp, color = Color.Gray)
                Text(text = value, fontSize = 20.sp, fontWeight = FontWeight.Bold, color = textColor)
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUsersTab(isDarkMode: Boolean, isHighContrast: Boolean) {
    val context = LocalContext.current
    var searchQuery by remember { mutableStateOf("") }
    var debouncedQuery by remember { mutableStateOf("") }
    var usersList by remember { mutableStateOf<List<AdminUserItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(false) }

    val isDark = isDarkMode || isHighContrast
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryTextColor = if (isDark) Color.LightGray else Color.Gray
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White

    // 300ms Debounce search input logic
    LaunchedEffect(searchQuery) {
        delay(300)
        debouncedQuery = searchQuery
    }

    LaunchedEffect(debouncedQuery) {
        isLoading = true
        val token = SecureStore.getToken(context)
        if (token == null) {
            isLoading = false
            return@LaunchedEffect
        }
        try {
            val res = adminApiService.getUsers("Bearer $token", debouncedQuery)
            if (res.isSuccessful) {
                usersList = res.body()?.users ?: emptyList()
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Debounced Search Input
        TextField(
            value = searchQuery,
            onValueChange = { searchQuery = it },
            placeholder = { Text("Search users by name, email...", color = Color.Gray) },
            leadingIcon = { Icon(Icons.Default.Search, contentDescription = null, tint = Color.LightGray) },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedContainerColor = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF0F5FC),
                unfocusedContainerColor = if (isDark) Color(0xFF1E1E1E) else Color(0xFFF0F5FC),
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        )

        if (isLoading) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = if (isHighContrast) Color.White else Color(0xFF2196F3))
            }
        } else if (usersList.isEmpty()) {
            Box(modifier = Modifier.fillMaxWidth().weight(1f), contentAlignment = Alignment.Center) {
                Text("No users found", color = Color.Gray, fontSize = 14.sp)
            }
        } else {
            LazyColumn(
                modifier = Modifier.fillMaxWidth().weight(1f),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                items(usersList) { user ->
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(16.dp),
                        color = cardColor,
                        shadowElevation = if (isHighContrast) 0.dp else 1.dp,
                        border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Surface(
                                modifier = Modifier.size(40.dp),
                                shape = CircleShape,
                                color = if (isDark) Color(0xFF333333) else Color(0xFFE3F2FD)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(imageVector = Icons.Default.Person, contentDescription = null, tint = Color(0xFF2196F3))
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = user.name,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor
                                )
                                Text(
                                    text = user.email,
                                    fontSize = 13.sp,
                                    color = secondaryTextColor,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                                if (!user.phone.isNullOrBlank()) {
                                    Text(
                                        text = "Phone: ${user.phone}",
                                        fontSize = 12.sp,
                                        color = Color.Gray
                                    )
                                }
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isDark) Color(0xFF333333) else Color(0xFFF1F1F1)
                            ) {
                                Text(
                                    text = user.language?.uppercase() ?: "EN",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = textColor,
                                    modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun AdminFormsTab(isDarkMode: Boolean, isHighContrast: Boolean, viewModel: FormViewModel) {
    val context = LocalContext.current
    var formsList by remember { mutableStateOf<List<AdminFormItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }
    var selectedForm by remember { mutableStateOf<AdminFormItem?>(null) }

    val isDark = isDarkMode || isHighContrast
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryTextColor = if (isDark) Color.LightGray else Color.Gray
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White

    LaunchedEffect(Unit) {
        val token = SecureStore.getToken(context)
        if (token == null) {
            errorMsg = "Unauthorized"
            isLoading = false
            return@LaunchedEffect
        }
        try {
            val res = adminApiService.getForms("Bearer $token")
            if (res.isSuccessful) {
                formsList = res.body()?.forms ?: emptyList()
            } else {
                errorMsg = "Failed to load forms: ${res.message()}"
            }
        } catch (e: Exception) {
            errorMsg = "Error: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = if (isHighContrast) Color.White else Color(0xFF2196F3))
        }
    } else if (errorMsg != null) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
            Text(errorMsg!!, color = Color.Red, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        }
    } else if (formsList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No uploaded forms found", color = Color.Gray, fontSize = 14.sp)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(formsList) { form ->
                Surface(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable { selectedForm = form },
                    shape = RoundedCornerShape(16.dp),
                    color = cardColor,
                    shadowElevation = if (isHighContrast) 0.dp else 1.dp,
                    border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Surface(
                            modifier = Modifier.size(40.dp),
                            shape = RoundedCornerShape(8.dp),
                            color = if (isDark) Color(0xFF333333) else Color(0xFFFFF3E0)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(imageVector = Icons.Default.Description, contentDescription = null, tint = Color(0xFFFF9800))
                            }
                        }
                        Spacer(modifier = Modifier.width(16.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = form.fileName,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "By: ${form.userEmail}",
                                fontSize = 13.sp,
                                color = secondaryTextColor,
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(
                                text = "Date: ${form.createdAt}",
                                fontSize = 12.sp,
                                color = Color.Gray
                            )
                        }
                        Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null, tint = Color.LightGray)
                    }
                }
            }
        }
    }

    // Details Dialog
    if (selectedForm != null) {
        FormDetailsDialog(
            form = selectedForm!!,
            viewModel = viewModel,
            isDarkMode = isDarkMode,
            isHighContrast = isHighContrast,
            onDismiss = {
                viewModel.stopAudio()
                selectedForm = null
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FormDetailsDialog(
    form: AdminFormItem,
    viewModel: FormViewModel,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onDismiss: () -> Unit
) {
    val context = LocalContext.current
    val isDark = isDarkMode || isHighContrast
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryTextColor = if (isDark) Color.LightGray else Color.Gray

    val isAudioPlaying = viewModel.isAudioPlaying && viewModel.currentlyPlayingUrl == form.audioPath

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth(0.95f)
                .fillMaxHeight(0.85f)
                .clip(RoundedCornerShape(24.dp)),
            color = cardColor,
            border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
        ) {
            Scaffold(
                topBar = {
                    TopAppBar(
                        title = { Text("Form Log Details", fontWeight = FontWeight.Bold, fontSize = 18.sp, color = textColor) },
                        navigationIcon = {
                            IconButton(onClick = onDismiss) {
                                Icon(imageVector = Icons.Default.Close, contentDescription = "Close", tint = textColor)
                            }
                        },
                        colors = TopAppBarDefaults.topAppBarColors(containerColor = cardColor)
                    )
                },
                containerColor = cardColor
            ) { paddingValues ->
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(paddingValues)
                        .verticalScroll(rememberScrollState())
                        .padding(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    // Image preview if available
                    if (!form.fileUrl.isNullOrEmpty()) {
                        Text("Scanned Form Image", fontWeight = FontWeight.Bold, color = textColor, fontSize = 14.sp)
                        Surface(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                            shape = RoundedCornerShape(12.dp),
                            color = if (isDark) Color(0xFF333333) else Color(0xFFF1F1F1)
                        ) {
                            AsyncImage(
                                model = UrlHelper.cleanUrl(form.fileUrl),
                                contentDescription = "Form Scanned Image",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Fit
                            )
                        }
                    }

                    // Extracted Text
                    Text("Extracted OCR Text", fontWeight = FontWeight.Bold, color = textColor, fontSize = 14.sp)
                    Surface(
                        modifier = Modifier
                            .fillMaxWidth()
                            .heightIn(max = 120.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isDark) Color(0xFF333333) else Color(0xFFF9F9F9),
                        border = BorderStroke(1.dp, if (isDark) Color(0xFF444444) else Color(0xFFE0E0E0))
                    ) {
                        Box(modifier = Modifier.padding(12.dp).verticalScroll(rememberScrollState())) {
                            Text(
                                text = form.extractedText ?: "No text extracted",
                                fontSize = 13.sp,
                                color = textColor,
                                fontFamily = androidx.compose.ui.text.font.FontFamily.Monospace
                            )
                        }
                    }

                    // AI Guidance
                    Text("AI Guidance Text", fontWeight = FontWeight.Bold, color = textColor, fontSize = 14.sp)
                    Surface(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isDark) Color(0xFF333333) else Color(0xFFF9F9F9),
                        border = BorderStroke(1.dp, if (isDark) Color(0xFF444444) else Color(0xFFE0E0E0))
                    ) {
                        Text(
                            text = form.guidanceText ?: "No AI guidance text generated",
                            fontSize = 13.sp,
                            color = textColor,
                            modifier = Modifier.padding(12.dp)
                        )
                    }

                    // Audio Guidance Player
                    if (!form.audioPath.isNullOrEmpty()) {
                        Text("Audio Guidance", fontWeight = FontWeight.Bold, color = textColor, fontSize = 14.sp)
                        Surface(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            color = if (isDark) Color(0xFF333333) else Color(0xFFE3F2FD),
                            border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
                        ) {
                            Row(
                                modifier = Modifier.padding(16.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Surface(
                                    modifier = Modifier
                                        .size(40.dp)
                                        .clickable {
                                            if (isAudioPlaying) {
                                                viewModel.stopAudio()
                                            } else {
                                                viewModel.playHistoryAudio(form.audioPath)
                                            }
                                        },
                                    shape = CircleShape,
                                    color = if (isHighContrast) Color.White else Color(0xFF2196F3)
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Icon(
                                            imageVector = if (isAudioPlaying) Icons.Default.Stop else Icons.Default.PlayArrow,
                                            contentDescription = if (isAudioPlaying) "Stop" else "Play",
                                            tint = if (isHighContrast) Color.Black else Color.White,
                                            modifier = Modifier.size(24.dp)
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.width(16.dp))
                                Column {
                                    Text(
                                        text = if (isAudioPlaying) "Playing Audio Guidance..." else "Audio Guidance Available",
                                        fontSize = 14.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = textColor
                                    )
                                    Text(
                                        text = "Stream the regional audio tutorial",
                                        fontSize = 12.sp,
                                        color = secondaryTextColor
                                    )
                                }
                            }
                        }
                    }

                    // Download PDF Button
                    if (!form.pdfUrl.isNullOrEmpty()) {
                        Button(
                            onClick = { viewModel.downloadSpecificPdf(context, form.pdfUrl) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = if (isHighContrast) Color.Yellow else Color(0xFF4CAF50),
                                contentColor = if (isHighContrast) Color.Black else Color.White
                            )
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(imageVector = Icons.Default.Download, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text("Download Report PDF", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                    
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
    }
}

@Composable
fun AdminFeedbackTab(isDarkMode: Boolean, isHighContrast: Boolean) {
    val context = LocalContext.current
    var feedbackList by remember { mutableStateOf<List<AdminFeedbackItem>>(emptyList()) }
    var isLoading by remember { mutableStateOf(true) }
    var errorMsg by remember { mutableStateOf<String?>(null) }

    val isDark = isDarkMode || isHighContrast
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryTextColor = if (isDark) Color.LightGray else Color.Gray
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White

    LaunchedEffect(Unit) {
        val token = SecureStore.getToken(context)
        if (token == null) {
            errorMsg = "Unauthorized"
            isLoading = false
            return@LaunchedEffect
        }
        try {
            val res = adminApiService.getFeedback("Bearer $token")
            if (res.isSuccessful) {
                feedbackList = res.body()?.feedback ?: emptyList()
            } else {
                errorMsg = "Failed to load feedbacks: ${res.message()}"
            }
        } catch (e: Exception) {
            errorMsg = "Error: ${e.localizedMessage}"
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = if (isHighContrast) Color.White else Color(0xFF2196F3))
        }
    } else if (errorMsg != null) {
        Box(modifier = Modifier.fillMaxSize().padding(16.dp), contentAlignment = Alignment.Center) {
            Text(errorMsg!!, color = Color.Red, textAlign = TextAlign.Center, fontWeight = FontWeight.Bold)
        }
    } else if (feedbackList.isEmpty()) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("No feedback entries found", color = Color.Gray, fontSize = 14.sp)
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(feedbackList) { item ->
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    color = cardColor,
                    shadowElevation = if (isHighContrast) 0.dp else 1.dp,
                    border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = item.userEmail,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = textColor,
                                modifier = Modifier.weight(1f),
                                maxLines = 1,
                                overflow = TextOverflow.Ellipsis
                            )
                            // Stars Row
                            val ratingStars = item.rating ?: 5
                            Row {
                                repeat(5) { starIndex ->
                                    Icon(
                                        imageVector = if (starIndex < ratingStars) Icons.Default.Star else Icons.Default.StarBorder,
                                        contentDescription = null,
                                        tint = Color(0xFFFFB300),
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(text = "App Experience: ${item.appExperience}", fontSize = 12.sp, color = secondaryTextColor)
                        Text(text = "Voice Guidance: ${item.voiceGuidanceHelpful}", fontSize = 12.sp, color = secondaryTextColor)
                        Text(text = "Would Recommend: ${item.recommendApp}", fontSize = 12.sp, color = secondaryTextColor)
                        
                        if (!item.additionalComments.isNullOrBlank()) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isDark) Color(0xFF333333) else Color(0xFFF9F9F9),
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(
                                    text = item.additionalComments,
                                    fontSize = 13.sp,
                                    color = textColor,
                                    modifier = Modifier.padding(10.dp)
                                )
                            }
                        }
                        
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Submitted: ${item.createdAt}",
                            fontSize = 10.sp,
                            color = Color.Gray,
                            modifier = Modifier.align(Alignment.End)
                        )
                    }
                }
            }
        }
    }
}

/**
 * Custom line chart drawn using Jetpack Compose Canvas
 */
@Composable
fun LineChart(
    dataPoints: List<Pair<String, Float>>,
    modifier: Modifier = Modifier,
    lineColor: Color = Color(0xFF2196F3),
    textColor: Color = Color.Gray
) {
    val density = LocalDensity.current
    val textPaint = remember(textColor, density) {
        Paint().apply {
            color = textColor.toArgb()
            textSize = with(density) { 9.sp.toPx() }
            textAlign = Paint.Align.RIGHT
        }
    }
    val bottomTextPaint = remember(textColor, density) {
        Paint().apply {
            color = textColor.toArgb()
            textSize = with(density) { 8.sp.toPx() }
            textAlign = Paint.Align.CENTER
        }
    }

    Canvas(modifier = modifier) {
        if (dataPoints.isEmpty()) return@Canvas

        val paddingLeft = 40.dp.toPx()
        val paddingRight = 10.dp.toPx()
        val paddingTop = 10.dp.toPx()
        val paddingBottom = 25.dp.toPx()

        val chartWidth = size.width - paddingLeft - paddingRight
        val chartHeight = size.height - paddingTop - paddingBottom

        val maxVal = dataPoints.maxOfOrNull { it.second } ?: 10f
        val maxY = if (maxVal == 0f) 10f else maxVal * 1.2f

        // Draw horizontal grid lines and Y-axis labels
        val gridLines = 4
        for (i in 0..gridLines) {
            val ratio = i.toFloat() / gridLines
            val y = paddingTop + chartHeight * (1 - ratio)
            
            // Draw gridline
            drawLine(
                color = Color.LightGray.copy(alpha = 0.3f),
                start = Offset(paddingLeft, y),
                end = Offset(size.width - paddingRight, y),
                strokeWidth = 1.dp.toPx()
            )

            // Draw Y label
            val yValStr = String.format("%.0f", ratio * maxY)
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    yValStr,
                    paddingLeft - 10.dp.toPx(),
                    y + 3.dp.toPx(),
                    textPaint
                )
            }
        }

        // Draw line and points
        val stepX = if (dataPoints.size > 1) chartWidth / (dataPoints.size - 1) else chartWidth
        val points = dataPoints.mapIndexed { index, pair ->
            val x = paddingLeft + index * stepX
            val ratio = pair.second / maxY
            val y = paddingTop + chartHeight * (1 - ratio)
            Offset(x, y)
        }

        // Draw line path
        val path = Path().apply {
            if (points.isNotEmpty()) {
                moveTo(points.first().x, points.first().y)
                for (i in 1 until points.size) {
                    lineTo(points[i].x, points[i].y)
                }
            }
        }

        drawPath(
            path = path,
            color = lineColor,
            style = Stroke(width = 3.dp.toPx())
        )

        // Draw gradient area under the line
        if (points.isNotEmpty()) {
            val fillPath = Path().apply {
                moveTo(points.first().x, paddingTop + chartHeight)
                for (point in points) {
                    lineTo(point.x, point.y)
                }
                lineTo(points.last().x, paddingTop + chartHeight)
                close()
            }
            drawPath(
                path = fillPath,
                brush = Brush.verticalGradient(
                    colors = listOf(lineColor.copy(alpha = 0.35f), Color.Transparent),
                    startY = points.minOf { it.y },
                    endY = paddingTop + chartHeight
                )
            )
        }

        // Draw dot points and X-axis labels
        dataPoints.forEachIndexed { index, pair ->
            val point = points[index]

            // Draw Circle Dot
            drawCircle(
                color = lineColor,
                radius = 4.dp.toPx(),
                center = point
            )
            drawCircle(
                color = Color.White,
                radius = 2.dp.toPx(),
                center = point
            )

            // Draw X label (only for some points if data is large, or all if small)
            val dateLabel = if (pair.first.length > 5) pair.first.substring(5) else pair.first
            drawIntoCanvas { canvas ->
                canvas.nativeCanvas.drawText(
                    dateLabel,
                    point.x,
                    size.height - 5.dp.toPx(),
                    bottomTextPaint
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDeveloperTab(isDarkMode: Boolean, isHighContrast: Boolean) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()

    var name by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var github by remember { mutableStateOf("") }
    var linkedin by remember { mutableStateOf("") }
    var portfolio by remember { mutableStateOf("") }
    var currentProfileImageUrl by remember { mutableStateOf<String?>(null) }
    
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var selectedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    var isLoading by remember { mutableStateOf(true) }
    var isSaving by remember { mutableStateOf(false) }

    val isDark = isDarkMode || isHighContrast
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryTextColor = if (isDark) Color.LightGray else Color.Gray
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White

    // File pickers
    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri ->
        uri?.let {
            selectedImageUri = it
            try {
                val source = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                    ImageDecoder.createSource(context.contentResolver, it)
                } else {
                    null
                }
                selectedImageBitmap = if (source != null) {
                    ImageDecoder.decodeBitmap(source)
                } else {
                    @Suppress("DEPRECATION")
                    MediaStore.Images.Media.getBitmap(context.contentResolver, it)
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    LaunchedEffect(Unit) {
        try {
            val res = RetrofitClient.apiService.getDeveloperDetails()
            if (res.isSuccessful && res.body() != null) {
                val details = res.body()!!
                name = details.name ?: ""
                fatherName = details.fatherName ?: ""
                role = details.role ?: ""
                description = details.description ?: ""
                email = details.email ?: ""
                github = details.github ?: ""
                linkedin = details.linkedin ?: ""
                portfolio = details.portfolio ?: ""
                currentProfileImageUrl = details.profileImage
            }
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
            isLoading = false
        }
    }

    if (isLoading) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = if (isHighContrast) Color.White else Color(0xFF2196F3))
        }
    } else {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Text(
                text = "Edit Developer Details",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = textColor
            )

            // Image Selection Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                color = cardColor,
                shadowElevation = if (isHighContrast) 0.dp else 2.dp,
                border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Avatar Image
                    Surface(
                        modifier = Modifier.size(80.dp),
                        shape = RoundedCornerShape(12.dp),
                        color = if (isDark) Color(0xFF333333) else Color(0xFFF0F0F0),
                        border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
                    ) {
                        if (selectedImageBitmap != null) {
                            Image(
                                bitmap = selectedImageBitmap!!.asImageBitmap(),
                                contentDescription = "Selected Photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else if (!currentProfileImageUrl.isNullOrEmpty()) {
                            AsyncImage(
                                model = UrlHelper.cleanUrl(currentProfileImageUrl),
                                contentDescription = "Current Photo",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(Icons.Default.Person, contentDescription = null, modifier = Modifier.size(40.dp))
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(16.dp))

                    Button(
                        onClick = { galleryLauncher.launch("image/*") },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isHighContrast) Color.Yellow else Color(0xFF2196F3),
                            contentColor = if (isHighContrast) Color.Black else Color.White
                        )
                    ) {
                        Text("Change Photo", fontWeight = FontWeight.Bold)
                    }
                }
            }

            // Input Fields
            AdminTextField(value = name, onValueChange = { name = it }, label = "Developer Name", isDark = isDark, textColor = textColor)
            AdminTextField(value = fatherName, onValueChange = { fatherName = it }, label = "Father's Name", isDark = isDark, textColor = textColor)
            AdminTextField(value = role, onValueChange = { role = it }, label = "Role / Title", isDark = isDark, textColor = textColor)
            AdminTextField(value = description, onValueChange = { description = it }, label = "Description", isDark = isDark, textColor = textColor, singleLine = false)
            AdminTextField(value = email, onValueChange = { email = it }, label = "Email", isDark = isDark, textColor = textColor)
            AdminTextField(value = github, onValueChange = { github = it }, label = "GitHub Link", isDark = isDark, textColor = textColor)
            AdminTextField(value = linkedin, onValueChange = { linkedin = it }, label = "LinkedIn Link", isDark = isDark, textColor = textColor)
            AdminTextField(value = portfolio, onValueChange = { portfolio = it }, label = "Portfolio Link", isDark = isDark, textColor = textColor)

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isSaving = true
                    scope.launch {
                        val token = SecureStore.getToken(context)
                        if (token == null) {
                            Toast.makeText(context, "Unauthorized: No token found", Toast.LENGTH_SHORT).show()
                            isSaving = false
                            return@launch
                        }

                        try {
                            val namePart = name.trim().toRequestBody("text/plain".toMediaTypeOrNull())
                            val fatherPart = fatherName.trim().toRequestBody("text/plain".toMediaTypeOrNull())
                            val rolePart = role.trim().toRequestBody("text/plain".toMediaTypeOrNull())
                            val descPart = description.trim().toRequestBody("text/plain".toMediaTypeOrNull())
                            val emailPart = email.trim().toRequestBody("text/plain".toMediaTypeOrNull())
                            val githubPart = github.trim().toRequestBody("text/plain".toMediaTypeOrNull())
                            val linkedinPart = linkedin.trim().toRequestBody("text/plain".toMediaTypeOrNull())
                            val portfolioPart = portfolio.trim().toRequestBody("text/plain".toMediaTypeOrNull())

                            var imagePart: MultipartBody.Part? = null
                            if (selectedImageBitmap != null) {
                                val file = File(context.cacheDir, "dev_upload.jpg")
                                val bos = ByteArrayOutputStream()
                                selectedImageBitmap!!.compress(Bitmap.CompressFormat.JPEG, 90, bos)
                                val fos = FileOutputStream(file)
                                fos.write(bos.toByteArray())
                                fos.flush()
                                fos.close()

                                val requestFile = file.asRequestBody("image/jpeg".toMediaTypeOrNull())
                                imagePart = MultipartBody.Part.createFormData("profile_image", file.name, requestFile)
                            }

                            val res = adminApiService.updateDeveloperDetails(
                                authHeader = "Bearer $token",
                                name = namePart,
                                fatherName = fatherPart,
                                role = rolePart,
                                description = descPart,
                                email = emailPart,
                                github = githubPart,
                                linkedin = linkedinPart,
                                portfolio = portfolioPart,
                                profile_image = imagePart
                            )

                            if (res.isSuccessful) {
                                Toast.makeText(context, "Developer details updated successfully", Toast.LENGTH_SHORT).show()
                                // Update current image url if updated
                                val getRes = RetrofitClient.apiService.getDeveloperDetails()
                                if (getRes.isSuccessful && getRes.body() != null) {
                                    currentProfileImageUrl = getRes.body()!!.profileImage
                                    selectedImageBitmap = null
                                }
                            } else {
                                Toast.makeText(context, "Failed to update: ${res.message()}", Toast.LENGTH_SHORT).show()
                            }
                        } catch (e: Exception) {
                            Toast.makeText(context, "Error: ${e.localizedMessage}", Toast.LENGTH_SHORT).show()
                        } finally {
                            isSaving = false
                        }
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp),
                enabled = !isSaving,
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isHighContrast) Color.Yellow else Color(0xFF4CAF50),
                    contentColor = if (isHighContrast) Color.Black else Color.White
                )
            ) {
                if (isSaving) {
                    CircularProgressIndicator(color = if (isHighContrast) Color.Black else Color.White, modifier = Modifier.size(24.dp))
                } else {
                    Text("Save Developer Details", fontWeight = FontWeight.Bold, fontSize = 16.sp)
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun AdminTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isDark: Boolean,
    textColor: Color,
    singleLine: Boolean = true
) {
    Column(modifier = Modifier.fillMaxWidth()) {
        Text(text = label, fontSize = 12.sp, color = Color.Gray, fontWeight = FontWeight.Bold)
        Spacer(modifier = Modifier.height(6.dp))
        TextField(
            value = value,
            onValueChange = onValueChange,
            modifier = Modifier.fillMaxWidth(),
            singleLine = singleLine,
            maxLines = if (singleLine) 1 else 5,
            colors = TextFieldDefaults.colors(
                focusedContainerColor = if (isDark) Color(0xFF2C2C2C) else Color(0xFFF1F5F9),
                unfocusedContainerColor = if (isDark) Color(0xFF2C2C2C) else Color(0xFFF1F5F9),
                focusedTextColor = textColor,
                unfocusedTextColor = textColor,
                focusedIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent
            ),
            shape = RoundedCornerShape(12.dp)
        )
    }
}
