package com.simats.formsahayak.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.draw.clip
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.simats.formsahayak.R
import com.simats.formsahayak.logic.RetrofitClient
import com.simats.formsahayak.logic.UrlHelper

/**
 * Configurable Constants for Developer Links
 */
object DeveloperConfig {
    const val EMAIL = "praneethyamanuri@gmail.com"
    const val GITHUB = "https://github.com/Praneeth5158"
    const val LINKEDIN = "https://www.linkedin.com/in/yamanuri-praneeth/"
    const val PORTFOLIO = "https://praneeth5158.github.io/My-Portfolio/" // Optional Portfolio link
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AboutDeveloperScreen(
    selectedLanguage: Language?,
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    onBackClick: () -> Unit
) {
    val isDark = isDarkMode || isHighContrast
    val backgroundColor = if (isDark) Color.Black else Color(0xFFF8FBFF)
    val cardColor = if (isDark) Color(0xFF1E1E1E) else Color.White
    val textColor = if (isDark) Color.White else Color.Black
    val secondaryTextColor = if (isDark) Color.LightGray else Color.Gray
    val uriHandler = LocalUriHandler.current

    var name by remember { mutableStateOf("") }
    var fatherName by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("") }
    var description by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var github by remember { mutableStateOf("") }
    var linkedin by remember { mutableStateOf("") }
    var portfolio by remember { mutableStateOf("") }
    var profileImageUrl by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(true) }

    val defaultName = stringResource(R.string.developer_name)
    val defaultFatherName = stringResource(R.string.developer_father_name)
    val defaultRole = stringResource(R.string.developer_role)
    val defaultDesc = stringResource(R.string.developer_desc)

    LaunchedEffect(Unit) {
        try {
            val res = RetrofitClient.apiService.getDeveloperDetails()
            if (res.isSuccessful && res.body() != null) {
                val details = res.body()!!
                name = details.name ?: defaultName
                fatherName = details.fatherName ?: defaultFatherName
                role = details.role ?: defaultRole
                description = details.description ?: defaultDesc
                email = details.email ?: DeveloperConfig.EMAIL
                github = details.github ?: DeveloperConfig.GITHUB
                linkedin = details.linkedin ?: DeveloperConfig.LINKEDIN
                portfolio = details.portfolio ?: DeveloperConfig.PORTFOLIO
                profileImageUrl = details.profileImage
            } else {
                name = defaultName
                fatherName = defaultFatherName
                role = defaultRole
                description = defaultDesc
                email = DeveloperConfig.EMAIL
                github = DeveloperConfig.GITHUB
                linkedin = DeveloperConfig.LINKEDIN
                portfolio = DeveloperConfig.PORTFOLIO
            }
        } catch (e: Exception) {
            e.printStackTrace()
            name = defaultName
            fatherName = defaultFatherName
            role = defaultRole
            description = defaultDesc
            email = DeveloperConfig.EMAIL
            github = DeveloperConfig.GITHUB
            linkedin = DeveloperConfig.LINKEDIN
            portfolio = DeveloperConfig.PORTFOLIO
        } finally {
            isLoading = false
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        text = stringResource(R.string.about_developer), 
                        fontWeight = FontWeight.ExtraBold, 
                        fontSize = 20.sp, 
                        color = textColor
                    ) 
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack, 
                            contentDescription = stringResource(R.string.back), 
                            tint = textColor
                        )
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
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Profile Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = cardColor,
                shadowElevation = if (isHighContrast) 0.dp else 4.dp,
                border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
            ) {
                Column(
                    modifier = Modifier.padding(24.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    // Styled Image Avatar for Developer (Rounded Rectangle - shows full face & hair)
                    Surface(
                        modifier = Modifier
                            .width(135.dp)
                            .height(170.dp),
                        shape = RoundedCornerShape(16.dp),
                        color = if (isDark) Color(0xFF333333) else Color(0xFFE3F2FD),
                        border = if (isHighContrast) BorderStroke(3.dp, Color.White) else BorderStroke(3.dp, Color(0xFF2196F3))
                    ) {
                        if (isLoading) {
                            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                                CircularProgressIndicator(color = if (isHighContrast) Color.White else Color(0xFF2196F3))
                            }
                        } else if (!profileImageUrl.isNullOrEmpty()) {
                            AsyncImage(
                                model = UrlHelper.cleanUrl(profileImageUrl),
                                contentDescription = name,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        } else {
                            Image(
                                painter = painterResource(id = R.drawable.developer_profile),
                                contentDescription = name,
                                modifier = Modifier
                                    .fillMaxSize()
                                    .clip(RoundedCornerShape(16.dp)),
                                contentScale = ContentScale.Crop
                            )
                        }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (isLoading) "..." else name, 
                        fontSize = 22.sp, 
                        fontWeight = FontWeight.ExtraBold, 
                        color = textColor,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = if (isLoading) "..." else fatherName, 
                        fontSize = 14.sp, 
                        fontWeight = FontWeight.Medium, 
                        color = secondaryTextColor,
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = if (isLoading) "..." else role, 
                        fontSize = 14.sp, 
                        fontWeight = FontWeight.SemiBold,
                        color = if (isHighContrast) Color.White else Color(0xFF2196F3),
                        textAlign = TextAlign.Center
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = if (isLoading) "..." else description, 
                        fontSize = 13.sp, 
                        color = secondaryTextColor,
                        lineHeight = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Connect Section Card
            if (!isLoading) {
                Surface(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(24.dp),
                    color = cardColor,
                    shadowElevation = if (isHighContrast) 0.dp else 4.dp,
                    border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = stringResource(R.string.connect), 
                            fontSize = 16.sp, 
                            fontWeight = FontWeight.ExtraBold, 
                            color = textColor
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        
                        // Email Row
                        if (email.isNotEmpty()) {
                            ContactRow(
                                icon = Icons.Default.Email,
                                label = "Email",
                                value = email,
                                iconColor = Color(0xFF2196F3),
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor,
                                isHighContrast = isHighContrast,
                                onClick = { uriHandler.openUri("mailto:$email") }
                            )
                        }
                        
                        // GitHub Row
                        if (github.isNotEmpty()) {
                            if (email.isNotEmpty()) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = if (isDark) Color(0xFF333333) else Color(0xFFF0F0F0))
                            }
                            ContactRow(
                                icon = Icons.Default.Code,
                                label = "GitHub",
                                value = github.removePrefix("https://").removePrefix("www."),
                                iconColor = Color(0xFF333333),
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor,
                                isHighContrast = isHighContrast,
                                onClick = { uriHandler.openUri(github) }
                            )
                        }
                        
                        // LinkedIn Row
                        if (linkedin.isNotEmpty()) {
                            if (email.isNotEmpty() || github.isNotEmpty()) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = if (isDark) Color(0xFF333333) else Color(0xFFF0F0F0))
                            }
                            ContactRow(
                                icon = Icons.Default.Link,
                                label = "LinkedIn",
                                value = linkedin.removePrefix("https://").removePrefix("www.").removePrefix("linkedin.com/in/"),
                                iconColor = Color(0xFF0077B5),
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor,
                                isHighContrast = isHighContrast,
                                onClick = { uriHandler.openUri(linkedin) }
                            )
                        }
                        
                        // Portfolio Row (Optional)
                        if (portfolio.isNotEmpty()) {
                            if (email.isNotEmpty() || github.isNotEmpty() || linkedin.isNotEmpty()) {
                                HorizontalDivider(modifier = Modifier.padding(vertical = 12.dp), color = if (isDark) Color(0xFF333333) else Color(0xFFF0F0F0))
                            }
                            ContactRow(
                                icon = Icons.Default.Language,
                                label = "Portfolio",
                                value = portfolio.removePrefix("https://").removePrefix("www."),
                                iconColor = Color(0xFF4CAF50),
                                textColor = textColor,
                                secondaryTextColor = secondaryTextColor,
                                isHighContrast = isHighContrast,
                                onClick = { uriHandler.openUri(portfolio) }
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Future Updates Card
            Surface(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(24.dp),
                color = cardColor,
                shadowElevation = if (isHighContrast) 0.dp else 4.dp,
                border = if (isHighContrast) BorderStroke(2.dp, Color.White) else null
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Text(
                        text = stringResource(R.string.future_updates), 
                        fontSize = 16.sp, 
                        fontWeight = FontWeight.ExtraBold, 
                        color = textColor
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    
                    val updatesList = listOf(
                        Pair(Icons.AutoMirrored.Filled.VolumeUp, stringResource(R.string.voice_guidance_opt)),
                        Pair(Icons.Default.Translate, stringResource(R.string.more_regional_languages)),
                        Pair(Icons.Default.WifiOff, stringResource(R.string.offline_form_assistance)),
                        Pair(Icons.Default.Description, stringResource(R.string.government_form_templates)),
                        Pair(Icons.Default.SmartToy, stringResource(R.string.ai_chat_support))
                    )
                    
                    updatesList.forEachIndexed { index, item ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            val iconBgColor = when (index) {
                                0 -> Color(0xFFE3F2FD)
                                1 -> Color(0xFFF3E5F5)
                                2 -> Color(0xFFFFEBEE)
                                3 -> Color(0xFFE8F5E9)
                                else -> Color(0xFFFFF3E0)
                            }
                            val iconTint = when (index) {
                                0 -> Color(0xFF2196F3)
                                1 -> Color(0xFF9C27B0)
                                2 -> Color(0xFFE57373)
                                3 -> Color(0xFF4CAF50)
                                else -> Color(0xFFFF9800)
                            }
                            
                            Surface(
                                modifier = Modifier.size(32.dp),
                                shape = CircleShape,
                                color = if (isHighContrast) Color.Black else (if (isDark) Color(0xFF333333) else iconBgColor),
                                border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = item.first,
                                        contentDescription = null,
                                        tint = if (isHighContrast) Color.White else iconTint,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = item.second,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Medium,
                                color = textColor
                            )
                        }
                        if (index < updatesList.lastIndex) {
                            HorizontalDivider(modifier = Modifier.padding(vertical = 4.dp), color = if (isDark) Color(0xFF333333) else Color(0xFFF5F5F5))
                        }
                    }
                }
            }
            
            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
fun ContactRow(
    icon: ImageVector,
    label: String,
    value: String,
    iconColor: Color,
    textColor: Color,
    secondaryTextColor: Color,
    isHighContrast: Boolean,
    onClick: () -> Unit
) {
    val displayIconColor = if (isHighContrast) Color.White else iconColor
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() }
            .padding(vertical = 4.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Surface(
            modifier = Modifier.size(36.dp),
            shape = RoundedCornerShape(8.dp),
            color = displayIconColor.copy(alpha = 0.1f),
            border = if (isHighContrast) BorderStroke(1.dp, Color.White) else null
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = icon, 
                    contentDescription = null, 
                    tint = displayIconColor, 
                    modifier = Modifier.size(20.dp)
                )
            }
        }
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = label, fontSize = 11.sp, color = secondaryTextColor)
            Text(text = value, fontSize = 14.sp, fontWeight = FontWeight.Medium, color = textColor)
        }
        Icon(
            imageVector = Icons.Default.ChevronRight,
            contentDescription = "Open Link",
            tint = if (isHighContrast) Color.White else Color.LightGray,
            modifier = Modifier.size(20.dp)
        )
    }
}
