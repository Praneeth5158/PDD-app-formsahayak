package com.simats.formsahayak

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.LocalActivityResultRegistryOwner
import androidx.activity.result.ActivityResultRegistryOwner
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import com.simats.formsahayak.ui.screens.*
import com.simats.formsahayak.ui.theme.FormsahayakTheme
import com.simats.formsahayak.ui.viewmodel.FormViewModel
import com.simats.formsahayak.logic.SecureStore
import com.simats.formsahayak.logic.UserPrefs
import kotlinx.coroutines.delay
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val context = LocalContext.current
            var isDarkMode by remember { mutableStateOf(UserPrefs.isDarkMode(context)) }
            var isHighContrast by remember { mutableStateOf(UserPrefs.isHighContrast(context)) }
            val languages = listOf(
                Language("English", "English", "en"),
                Language("Telugu", "తెలుగు", "te"),
                Language("Tamil", "தமிழ்", "ta"),
                Language("Hindi", "हिन्दी", "hi")
            )
            var selectedLanguage by remember {
                mutableStateOf<Language?>(
                    UserPrefs.getLanguageCode(context)?.let { code ->
                        languages.find { it.code == code }
                    }
                )
            }
            val viewModel: FormViewModel = viewModel()

            LaunchedEffect(Unit) {
                if (UserPrefs.isLoggedIn(context)) {
                    val savedEmail = UserPrefs.getEmail(context)
                    if (savedEmail.isNotEmpty()) {
                        viewModel.fetchProfile(savedEmail)
                        viewModel.fetchHistory(savedEmail)
                    }
                }
            }

            LaunchedEffect(viewModel.loggedInUser?.language) {
                viewModel.loggedInUser?.language?.let { langCode ->
                    val matched = languages.find { it.code == langCode }
                    if (matched != null) {
                        selectedLanguage = matched
                    }
                }
            }

            val activityResultRegistryOwner = remember(context) { context as ActivityResultRegistryOwner }
            val localizedContext = remember(selectedLanguage) {
                val localeCode = selectedLanguage?.code ?: "en"
                val locale = java.util.Locale(localeCode)
                java.util.Locale.setDefault(locale)
                val config = android.content.res.Configuration(context.resources.configuration)
                config.setLocale(locale)
                context.createConfigurationContext(config)
            }

            CompositionLocalProvider(
                LocalContext provides localizedContext,
                LocalActivityResultRegistryOwner provides activityResultRegistryOwner
            ) {
                LaunchedEffect(selectedLanguage) {
                    viewModel.initTts(localizedContext)
                }

                FormsahayakTheme(
                    darkTheme = isDarkMode || isHighContrast
                ) {
                    MainApp(
                        isDarkMode = isDarkMode,
                        isHighContrast = isHighContrast,
                        selectedLanguage = selectedLanguage,
                        viewModel = viewModel,
                        onThemeChange = { 
                            isDarkMode = it
                            UserPrefs.setDarkMode(context, it)
                        },
                        onHighContrastChange = { 
                            isHighContrast = it
                            UserPrefs.setHighContrast(context, it)
                        },
                        onLanguageChange = { 
                            selectedLanguage = it
                            UserPrefs.setLanguageCode(context, it.code)
                        }
                    )
                }
            }
        }
    }
}

@Composable
fun MainApp(
    isDarkMode: Boolean,
    isHighContrast: Boolean,
    selectedLanguage: Language?,
    viewModel: FormViewModel,
    onThemeChange: (Boolean) -> Unit,
    onHighContrastChange: (Boolean) -> Unit,
    onLanguageChange: (Language) -> Unit
) {
    val context = LocalContext.current
    var currentScreen by remember { mutableStateOf("welcome") }
    var isLanguageChangeFromDashboard by remember { mutableStateOf(false) }
    
    // User Profile State derived from ViewModel
    val user = viewModel.loggedInUser
    val userName = user?.fullName ?: "Guest User"
    val userEmail = user?.emailOrPhone ?: ""
    val userPhone = user?.phone ?: ""

    // Navigation and Flow State
    var userAuthInput by remember { mutableStateOf("") }
    var formBitmap by remember { mutableStateOf<Bitmap?>(null) }

    // Profile Photo State
    var pickedImageBitmap by remember { mutableStateOf<Bitmap?>(null) }
    val tempProfileUri = remember {
        val file = File(context.cacheDir, "profile_capture.jpg")
        FileProvider.getUriForFile(context, "${context.packageName}.fileprovider", file)
    }

    val profileCameraLauncher = rememberLauncherForActivityResult(ActivityResultContracts.TakePicture()) { success ->
        if (success) {
            pickedImageBitmap = viewModel.getBitmapFromUri(context, tempProfileUri)
            currentScreen = "crop_photo"
        }
    }

    val profileGalleryLauncher = rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
        uri?.let {
            pickedImageBitmap = viewModel.getBitmapFromUri(context, it)
            currentScreen = "crop_photo"
        }
    }

    val profileCameraPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            profileCameraLauncher.launch(tempProfileUri)
        } else {
            val deniedMsg = when (selectedLanguage?.code) {
                "te" -> "ప్రొఫైల్ ఫోటో తీయడానికి కెమెరా అనుమతి అవసరం"
                "ta" -> "சுயவிவரப் புகைப்படத்தை எடுக்க கேமரா அனுமதி தேவை"
                "hi" -> "प्रोफ़ाइल फ़ोटो लेने के लिए कैमरा अनुमति की आवश्यकता है"
                else -> "Camera permission is required to take a profile photo"
            }
            Toast.makeText(context, deniedMsg, Toast.LENGTH_SHORT).show()
        }
    }

    val profileStoragePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
        Manifest.permission.READ_MEDIA_IMAGES
    } else {
        Manifest.permission.READ_EXTERNAL_STORAGE
    }

    val profileGalleryPermissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            profileGalleryLauncher.launch("image/*")
        } else {
            val deniedMsg = when (selectedLanguage?.code) {
                "te" -> "ఫోటోను ఎంచుకోవడానికి స్టోరేజ్ అనుమతి అవసరం"
                "ta" -> "புகைப்படத்தைத் தேர்ந்தெடுக்க சேமிப்பக அனுமதி தேவை"
                "hi" -> "फ़ोटो चुनने के लिए स्टोरेज अनुमति की आवश्यकता है"
                else -> "Storage permission is required to select a photo"
            }
            Toast.makeText(context, deniedMsg, Toast.LENGTH_SHORT).show()
        }
    }

    if (currentScreen == "welcome") {
        LaunchedEffect(Unit) {
            delay(3000)
            if (UserPrefs.isLoggedIn(context)) {
                val hasLanguage = UserPrefs.getLanguageCode(context) != null
                val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                    Manifest.permission.READ_MEDIA_IMAGES
                } else {
                    Manifest.permission.READ_EXTERNAL_STORAGE
                }
                val hasCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                val hasStorage = ContextCompat.checkSelfPermission(context, storagePermission) == PackageManager.PERMISSION_GRANTED
                val hasMic = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                val allPermissionsGranted = hasCamera && hasStorage && hasMic

                if (hasLanguage && allPermissionsGranted) {
                    currentScreen = "dashboard"
                } else if (!hasLanguage) {
                    currentScreen = "language"
                } else {
                    currentScreen = "grant_permissions"
                }
            } else {
                currentScreen = "onboarding"
            }
        }
    }

    Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
        Box(modifier = Modifier.padding(innerPadding)) {
            when (currentScreen) {
                "welcome" -> WelcomeScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onGetStarted = { currentScreen = "onboarding" }
                )
                "onboarding" -> OnboardingScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onFinished = { currentScreen = "login" }
                )
                "login" -> LoginScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    viewModel = viewModel,
                    onLoginSuccess = { 
                        val email = viewModel.loggedInUser?.emailOrPhone ?: ""
                        UserPrefs.setLoggedIn(context, true)
                        UserPrefs.setEmail(context, email)
                        
                        val hasLanguage = UserPrefs.getLanguageCode(context) != null
                        val storagePermission = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                            Manifest.permission.READ_MEDIA_IMAGES
                        } else {
                            Manifest.permission.READ_EXTERNAL_STORAGE
                        }
                        val hasCamera = ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED
                        val hasStorage = ContextCompat.checkSelfPermission(context, storagePermission) == PackageManager.PERMISSION_GRANTED
                        val hasMic = ContextCompat.checkSelfPermission(context, Manifest.permission.RECORD_AUDIO) == PackageManager.PERMISSION_GRANTED
                        val allPermissionsGranted = hasCamera && hasStorage && hasMic

                        if (hasLanguage && allPermissionsGranted) {
                            currentScreen = "dashboard"
                        } else if (!hasLanguage) {
                            currentScreen = "language"
                        } else {
                            currentScreen = "grant_permissions"
                        }
                    },
                    onNavigateToSignup = { currentScreen = "signup" },
                    onForgotPassword = { currentScreen = "forgot_password" },
                    onNavigateToAdminLogin = { currentScreen = "admin_login" }
                )
                "signup" -> SignupScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    viewModel = viewModel,
                    onRegisterClick = { currentScreen = "registration_success" },
                    onNavigateToLogin = { currentScreen = "login" }
                )
                "registration_success" -> RegistrationSuccessScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onBackToLogin = { currentScreen = "login" }
                )
                "forgot_password" -> ForgotPasswordScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onSendOtpClick = { input ->
                        viewModel.sendOtp(input) { success, msg ->
                            if (success) {
                                userAuthInput = input
                                currentScreen = "verify_otp"
                            } else {
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    onBackToLogin = { currentScreen = "login" }
                )
                "verify_otp" -> VerifyOtpScreen(
                    userInput = userAuthInput,
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onVerifyClick = { otpString ->
                        viewModel.verifyOtp(userAuthInput, otpString) { success, msg ->
                            if (success) {
                                currentScreen = "reset_password"
                            } else {
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    onResendOtpClick = { 
                        viewModel.sendOtp(userAuthInput) { success, msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            if (success) {
                                currentScreen = "otp_resent"
                            }
                        }
                    }
                )
                "otp_resent" -> OtpResentScreen(
                    userInput = userAuthInput,
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onVerifyClick = { otpString ->
                        viewModel.verifyOtp(userAuthInput, otpString) { success, msg ->
                            if (success) {
                                currentScreen = "reset_password"
                            } else {
                                Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            }
                        }
                    },
                    onBackToVerification = { currentScreen = "verify_otp" },
                    onResendOtp = {
                        viewModel.sendOtp(userAuthInput) { success, msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                )
                "verification_success" -> VerificationSuccessScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onBackToLogin = { currentScreen = "login" },
                    onGoToHome = { currentScreen = "dashboard" }
                )
                "verification_failed" -> VerificationFailedScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onTryAgain = { currentScreen = "forgot_password" },
                    onBackToLogin = { currentScreen = "login" },
                    onGoBack = { currentScreen = "verify_otp" }
                )
                "language" -> LanguageSelectionScreen(
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onContinueClick = { language ->
                        onLanguageChange(language)
                        if (isLanguageChangeFromDashboard) {
                            isLanguageChangeFromDashboard = false
                            currentScreen = "dashboard"
                        } else {
                            currentScreen = "grant_permissions"
                        }
                    }
                )
                "grant_permissions" -> GrantPermissionsScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onAllGranted = { currentScreen = "permissions_success" },
                    onBack = { currentScreen = "language" }
                )
                "permissions_success" -> AllPermissionsSetScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onContinue = { currentScreen = "dashboard" }
                )
                "dashboard" -> DashboardScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onChangeLanguage = { 
                        isLanguageChangeFromDashboard = true
                        currentScreen = "language" 
                    },
                    onNavigateToHowToUse = { currentScreen = "how_to_use" },
                    onNavigateToForms = { currentScreen = "forms" },
                    onNavigateToSettings = { currentScreen = "settings" },
                    onNavigateToProfile = { currentScreen = "profile" },
                    onUploadClick = { currentScreen = "upload_form" }
                )
                "upload_form" -> UploadFormScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onBackClick = { currentScreen = "dashboard" },
                    onImageSelected = { uri ->
                        formBitmap = viewModel.getBitmapFromUri(context, uri)
                        currentScreen = "scanning_edges"
                    }
                )
                "scanning_edges" -> ScanningProgressScreen(
                    imageBitmap = formBitmap,
                    selectedLanguage = selectedLanguage,
                    viewModel = viewModel,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    statusText = "Scanning...",
                    subText = "Detecting document edges...",
                    onScanningComplete = {
                        currentScreen = "auto_crop"
                    }
                )
                "auto_crop" -> AutoCropPreviewScreen(
                    imageBitmap = formBitmap,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onCancel = { currentScreen = "upload_form" },
                    onAccept = { cropped ->
                        if (cropped != null) {
                            formBitmap = viewModel.cropBitmap(cropped)
                        }
                        currentScreen = "enhance_image"
                    }
                )
                "enhance_image" -> EnhanceImageScreen(
                    imageBitmap = formBitmap,
                    viewModel = viewModel,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onBackClick = { currentScreen = "auto_crop" },
                    onApplyClick = { enhanced ->
                        if (enhanced != null) {
                            viewModel.addScannedPage(enhanced)
                        }
                        currentScreen = "multi_page_scan"
                    }
                )
                "multi_page_scan" -> MultiPageScanScreen(
                    scannedPages = viewModel.scannedPages,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onBackClick = { currentScreen = "enhance_image" },
                    onViewPage = { /* View logic if needed */ },
                    onDeletePage = { viewModel.removeScannedPage(it) },
                    onScanAnother = { currentScreen = "upload_form" },
                    onContinue = { 
                        viewModel.currentGuidingPageIndex = 0
                        currentScreen = "scanning_ocr" 
                    }
                )
                "scanning_ocr" -> {
                    LaunchedEffect(Unit) {
                        if (viewModel.scannedPages.isNotEmpty() && viewModel.currentGuidingPageIndex < viewModel.scannedPages.size) {
                            viewModel.analyzeImage(context, viewModel.scannedPages[viewModel.currentGuidingPageIndex], selectedLanguage) {}
                        }
                    }
                    ScanningProgressScreen(
                        imageBitmap = viewModel.scannedPages.getOrNull(viewModel.currentGuidingPageIndex),
                        selectedLanguage = selectedLanguage,
                        viewModel = viewModel,
                        isDarkMode = isDarkMode,
                        isHighContrast = isHighContrast,
                        statusText = "Analyzing Document",
                        subText = "Extracting fields and text for Page ${viewModel.currentGuidingPageIndex + 1}...",
                        onScanningComplete = {
                            currentScreen = if (viewModel.errorMessage != null) "image_unclear" else "form_detected"
                        }
                    )
                }
                "form_detected" -> FormDetectedScreen(
                    formType = viewModel.detectedFormType,
                    accountType = viewModel.detectedAccountType,
                    confidence = viewModel.detectedConfidence,
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    viewModel = viewModel,
                    onProceed = { currentScreen = "form_preview" },
                    onRescan = { currentScreen = "upload_form" },
                    onBack = { currentScreen = "dashboard" }
                )
                "image_unclear" -> ImageUnclearScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onTryAgain = { 
                        viewModel.clearError()
                        currentScreen = "upload_form" 
                    },
                    onBackToHome = { 
                        viewModel.clearError()
                        currentScreen = "dashboard" 
                    }
                )
                "form_preview" -> FormPreviewScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    viewModel = viewModel,
                    onBackClick = { currentScreen = "upload_form" },
                    onHomeClick = { currentScreen = "dashboard" },
                    onContinueClick = { 
                        if (viewModel.currentGuidingPageIndex < viewModel.scannedPages.size - 1) {
                            viewModel.currentGuidingPageIndex++
                            currentScreen = "scanning_ocr"
                        } else {
                            currentScreen = "form_completion"
                        }
                    },
                    onRetakeClick = { currentScreen = "upload_form" }
                )
                "form_completion" -> FormCompletionScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    viewModel = viewModel,
                    onFillAnotherClick = { currentScreen = "upload_form" },
                    onBackToHomeClick = { 
                        viewModel.reset()
                        currentScreen = "dashboard"
                    }
                )
                "feedback" -> FeedbackScreen(
                    selectedLanguage = selectedLanguage,
                    viewModel = viewModel,
                    onFinished = { currentScreen = "dashboard" },
                    onCancel = { currentScreen = "dashboard" }
                )
                "how_to_use" -> HowToUseScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onGotItClick = { currentScreen = "dashboard" },
                    onHomeClick = { currentScreen = "dashboard" },
                    onFormsClick = { currentScreen = "forms" },
                    onSettingsClick = { currentScreen = "settings" },
                    onProfileClick = { currentScreen = "profile" },
                    onVideoTutorialClick = { currentScreen = "video_tutorial" },
                    onGiveFeedbackClick = { currentScreen = "feedback" }
                )
                "video_tutorial" -> VideoTutorialScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onCloseClick = { currentScreen = "how_to_use" }
                )
                "forms" -> FormsScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    viewModel = viewModel,
                    onScanSelected = { currentScreen = "form_preview" },
                    onHomeClick = { currentScreen = "dashboard" },
                    onHelpClick = { currentScreen = "how_to_use" },
                    onSettingsClick = { currentScreen = "settings" },
                    onProfileClick = { currentScreen = "profile" },
                    onFormDetailsClick = { docId ->
                        viewModel.fetchFormDetails(docId) { success ->
                            if (success) {
                                currentScreen = "form_details"
                            } else {
                                Toast.makeText(context, "Failed to load form details", Toast.LENGTH_SHORT).show()
                            }
                        }
                    }
                )
                "form_details" -> FormDetailsScreen(
                    viewModel = viewModel,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onBack = { currentScreen = "forms" }
                )
                "settings" -> SettingsScreen(
                    currentLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    viewModel = viewModel,
                    onThemeChange = onThemeChange,
                    onHighContrastChange = onHighContrastChange,
                    onLanguageChange = onLanguageChange,
                    onHomeClick = { currentScreen = "dashboard" },
                    onFormsClick = { currentScreen = "forms" },
                    onHelpClick = { currentScreen = "how_to_use" },
                    onProfileClick = { currentScreen = "profile" },
                    onNavigateToVoiceSettings = { _ -> currentScreen = "voice_speed" }
                )
                "voice_speed" -> VoiceSpeedSelectionScreen(
                    currentSpeed = viewModel.voiceSpeed,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    selectedLanguage = selectedLanguage,
                    onSpeedSelected = { 
                        viewModel.updateVoiceSpeed(it)
                        currentScreen = "voice_guidance_detail"
                    },
                    onBackClick = { currentScreen = "settings" }
                )
                "voice_guidance_detail" -> VoiceGuidanceDetailScreen(
                    speed = viewModel.voiceSpeed,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    viewModel = viewModel,
                    onBackClick = { currentScreen = "voice_speed" }
                )
                "profile" -> ProfileScreen(
                    userName = userName,
                    userEmail = userEmail,
                    userPhone = userPhone,
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    viewModel = viewModel,
                    onEditProfileClick = { currentScreen = "edit_profile" },
                    onChangePasswordClick = { currentScreen = "change_password" },
                    onAboutDeveloperClick = { currentScreen = "about_developer" },
                    onLogoutClick = { 
                        UserPrefs.setLoggedIn(context, false)
                        UserPrefs.setEmail(context, "")
                        viewModel.logout()
                        currentScreen = "login" 
                    },
                    onHomeClick = { currentScreen = "dashboard" },
                    onFormsClick = { currentScreen = "forms" },
                    onHelpClick = { currentScreen = "how_to_use" },
                    onSettingsClick = { currentScreen = "settings" },
                    onCameraIconClick = { currentScreen = "update_photo_popup" }
                )
                "about_developer" -> AboutDeveloperScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onBackClick = { currentScreen = "profile" }
                )
                "admin_login" -> AdminLoginScreen(
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onLoginSuccess = { currentScreen = "admin_dashboard" },
                    onBackClick = { currentScreen = "login" }
                )
                "admin_dashboard" -> AdminDashboardScreen(
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    viewModel = viewModel,
                    onLogoutClick = {
                        SecureStore.clearToken(context)
                        viewModel.stopAudio()
                        currentScreen = "login"
                    }
                )
                "edit_profile" -> EditProfileScreen(
                    currentName = userName,
                    currentEmail = userEmail,
                    currentPhone = userPhone,
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    viewModel = viewModel,
                    onLanguageChange = onLanguageChange,
                    onBackClick = { currentScreen = "profile" },
                    onSaveClick = { name, email, phone ->
                        viewModel.updateProfile(
                            context = context,
                            phone = phone,
                            language = selectedLanguage?.code ?: "en",
                            imageBitmap = null // Update without changing photo here
                        ) { success, message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            if (success) {
                                currentScreen = "profile"
                            }
                        }
                    },
                    onChangePhotoClick = { currentScreen = "update_photo_popup" }
                )
                "update_photo_popup" -> UpdateProfilePhotoPopup(
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onDismiss = { 
                        currentScreen = "profile"
                    },
                    onTakePhoto = {
                        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {
                            profileCameraLauncher.launch(tempProfileUri)
                        } else {
                            profileCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
                        }
                    },
                    onUploadPhoto = {
                        if (ContextCompat.checkSelfPermission(context, profileStoragePermission) == PackageManager.PERMISSION_GRANTED) {
                            profileGalleryLauncher.launch("image/*")
                        } else {
                            profileGalleryPermissionLauncher.launch(profileStoragePermission)
                        }
                    }
                )
                "crop_photo" -> CropPhotoScreen(
                    pickedBitmap = pickedImageBitmap,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onCancel = { currentScreen = "profile" },
                    onSave = { croppedBitmap: Bitmap ->
                        viewModel.updateProfile(
                            context = context,
                            phone = userPhone,
                            language = selectedLanguage?.code ?: "en",
                            imageBitmap = croppedBitmap
                        ) { success, message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            if (success) {
                                currentScreen = "photo_updated"
                            } else {
                                currentScreen = "profile"
                            }
                        }
                    }
                )
                "photo_updated" -> ProfilePhotoUpdatedScreen(
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    userName = userName,
                    onGoToProfile = { currentScreen = "profile" }
                )
                "reset_password" -> ResetPasswordScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onResetPasswordClick = { newPass, confirmPass -> 
                        viewModel.resetPassword(userAuthInput, newPass, confirmPass) { success, msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                            if (success) {
                                currentScreen = "login"
                            }
                        }
                    },
                    onBackClick = { currentScreen = "login" },
                    isChangePassword = false
                )
                "change_password" -> ResetPasswordScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onResetPasswordClick = { newPass, confirmPass -> 
                        viewModel.changePassword(userEmail, newPass, confirmPass) { success, message ->
                            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
                            if (success) {
                                currentScreen = "password_updated_success"
                            }
                        }
                    },
                    onBackClick = { currentScreen = "profile" },
                    isChangePassword = true
                )
                "password_updated_success" -> PasswordUpdatedSuccessScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onBackToLogin = { 
                        viewModel.logout()
                        currentScreen = "login" 
                    }
                )
            }
        }
    }
}
