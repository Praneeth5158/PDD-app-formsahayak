package com.simats.formsahayak

import android.graphics.Bitmap
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
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
import kotlinx.coroutines.delay
import java.io.File

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            var isDarkMode by remember { mutableStateOf(false) }
            var isHighContrast by remember { mutableStateOf(false) }
            var selectedLanguage by remember { mutableStateOf<Language?>(null) }
            val viewModel: FormViewModel = viewModel()

            LaunchedEffect(Unit) {
                viewModel.initTts(this@MainActivity)
            }

            FormsahayakTheme(
                darkTheme = isDarkMode || isHighContrast
            ) {
                MainApp(
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    selectedLanguage = selectedLanguage,
                    viewModel = viewModel,
                    onThemeChange = { isDarkMode = it },
                    onHighContrastChange = { isHighContrast = it },
                    onLanguageChange = { selectedLanguage = it }
                )
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
    
    // User Profile State
    var userName by remember { mutableStateOf("Rajesh Kumar") }
    var userEmail by remember { mutableStateOf("rajesh.kumar@email.com") }
    var userPhone by remember { mutableStateOf("+91 98765 43210") }

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

    if (currentScreen == "welcome") {
        LaunchedEffect(Unit) {
            delay(3000)
            currentScreen = "onboarding"
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
                    onLoginSuccess = { currentScreen = "language" },
                    onNavigateToSignup = { currentScreen = "signup" },
                    onForgotPassword = { currentScreen = "forgot_password" }
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
                        userAuthInput = input
                        currentScreen = "verify_otp"
                    },
                    onBackToLogin = { currentScreen = "login" }
                )
                "verify_otp" -> VerifyOtpScreen(
                    userInput = userAuthInput,
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onVerifyClick = { isSuccess ->
                        currentScreen = if (isSuccess) "verification_success" else "verification_failed"
                    },
                    onResendOtpClick = { currentScreen = "otp_resent" }
                )
                "otp_resent" -> OtpResentScreen(
                    userInput = userAuthInput,
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onVerifyClick = { isSuccess ->
                        currentScreen = if (isSuccess) "verification_success" else "verification_failed"
                    },
                    onBackToVerification = { currentScreen = "verify_otp" }
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
                    onFillAnotherClick = { currentScreen = "upload_form" },
                    onBackToHomeClick = { 
                        viewModel.reset()
                        currentScreen = "dashboard"
                    }
                )
                "feedback" -> FeedbackScreen(
                    selectedLanguage = selectedLanguage,
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
                    onProfileClick = { currentScreen = "profile" }
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
                    onLogoutClick = { 
                        viewModel.logout()
                        currentScreen = "login" 
                    },
                    onHomeClick = { currentScreen = "dashboard" },
                    onFormsClick = { currentScreen = "forms" },
                    onHelpClick = { currentScreen = "how_to_use" },
                    onSettingsClick = { currentScreen = "settings" },
                    onCameraIconClick = { currentScreen = "update_photo_popup" }
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
                        userName = name
                        userEmail = email
                        userPhone = phone
                        currentScreen = "profile"
                    },
                    onChangePhotoClick = { currentScreen = "update_photo_popup" }
                )
                "update_photo_popup" -> UpdateProfilePhotoPopup(
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onDismiss = { 
                        currentScreen = "profile"
                    },
                    onTakePhoto = { profileCameraLauncher.launch(tempProfileUri) },
                    onUploadPhoto = { profileGalleryLauncher.launch("image/*") }
                )
                "crop_photo" -> CropPhotoScreen(
                    pickedBitmap = pickedImageBitmap,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onCancel = { currentScreen = "profile" },
                    onSave = { croppedBitmap: Bitmap ->
                        viewModel.updateProfilePicture(croppedBitmap)
                        currentScreen = "photo_updated"
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
                    onResetPasswordClick = { _, _ -> currentScreen = "login" },
                    onBackClick = { currentScreen = "login" },
                    isChangePassword = false
                )
                "change_password" -> ResetPasswordScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onResetPasswordClick = { _, _ -> currentScreen = "profile" },
                    onBackClick = { currentScreen = "profile" },
                    isChangePassword = true
                )
            }
        }
    }
}
