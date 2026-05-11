package com.simats.formsahayak

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import com.simats.formsahayak.ui.screens.*
import com.simats.formsahayak.ui.theme.FormsahayakTheme
import com.simats.formsahayak.ui.viewmodel.FormViewModel
import kotlinx.coroutines.delay

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
                darkTheme = isDarkMode,
                isHighContrast = isHighContrast
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
    var currentScreen by remember { mutableStateOf("welcome") }
    
    // User Profile State
    var userName by remember { mutableStateOf("Rajesh Kumar") }
    var userEmail by remember { mutableStateOf("rajesh.kumar@email.com") }
    var userPhone by remember { mutableStateOf("+91 98765 43210") }
    
    // Forgot Password Input State
    var forgotPasswordInput by remember { mutableStateOf("") }
    
    // Profile Photo Update States
    var showUpdatePhotoPopup by remember { mutableStateOf(false) }

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
                    onGetStarted = { currentScreen = "onboarding" }
                )
                "onboarding" -> OnboardingScreen(
                    selectedLanguage = selectedLanguage,
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
                "forgot_password" -> ForgotPasswordScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onSendOtpClick = { input -> 
                        forgotPasswordInput = input
                        currentScreen = "verify_otp" 
                    },
                    onBackToLogin = { currentScreen = "login" }
                )
                "verify_otp" -> VerifyOtpScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onVerifyClick = { isSuccess ->
                        currentScreen = if (isSuccess) "verification_success" else "verification_failed"
                    },
                    onResendOtpClick = { currentScreen = "otp_resent" }
                )
                "otp_resent" -> OtpResentScreen(
                    userInput = forgotPasswordInput,
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
                    onBackToLogin = { currentScreen = "login" },
                    onGoToHome = { currentScreen = "dashboard" }
                )
                "verification_failed" -> VerificationFailedScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    onTryAgain = { currentScreen = "verify_otp" },
                    onBackToLogin = { currentScreen = "login" },
                    onGoBack = { currentScreen = "verify_otp" }
                )
                "signup" -> SignupScreen(
                    selectedLanguage = selectedLanguage,
                    viewModel = viewModel,
                    onRegisterClick = { currentScreen = "login" },
                    onNavigateToLogin = { currentScreen = "login" }
                )
                "language" -> LanguageSelectionScreen(onContinueClick = { language ->
                    onLanguageChange(language)
                    currentScreen = "permissions"
                })
                "permissions" -> PermissionsScreen(
                    isDarkMode = isDarkMode,
                    onContinue = { currentScreen = "dashboard" },
                    onGoBack = { currentScreen = "language" }
                )
                "dashboard" -> DashboardScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onChangeLanguage = { currentScreen = "language" },
                    onNavigateToHowToUse = { currentScreen = "how_to_use" },
                    onNavigateToForms = { currentScreen = "forms" },
                    onNavigateToSettings = { currentScreen = "settings" },
                    onNavigateToProfile = { currentScreen = "profile" },
                    onUploadClick = { 
                        viewModel.reset()
                        currentScreen = "upload_form" 
                    }
                )
                "upload_form" -> UploadFormScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    viewModel = viewModel,
                    onBackClick = { currentScreen = "dashboard" },
                    onFormAnalyzed = {
                        if (viewModel.errorMessage != null || viewModel.detectedFields.isEmpty()) {
                            currentScreen = "image_unclear"
                        } else {
                            currentScreen = "form_detected"
                        }
                    }
                )
                "image_unclear" -> ImageUnclearScreen(
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onTryAgain = { 
                        viewModel.reset()
                        currentScreen = "upload_form" 
                    },
                    onBackToHome = { 
                        viewModel.reset()
                        currentScreen = "dashboard" 
                    }
                )
                "form_detected" -> FormDetectedScreen(
                    formType = viewModel.detectedFormType,
                    accountType = viewModel.detectedAccountType,
                    confidence = viewModel.detectedConfidence,
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    viewModel = viewModel,
                    onProceed = { currentScreen = "form_preview" },
                    onRescan = {
                        viewModel.reset()
                        currentScreen = "upload_form"
                    },
                    onBack = { currentScreen = "upload_form" }
                )
                "form_preview" -> FormPreviewScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    viewModel = viewModel,
                    onBackClick = { currentScreen = "form_detected" },
                    onHomeClick = { currentScreen = "dashboard" },
                    onContinueClick = { currentScreen = "form_completion" },
                    onRetakeClick = { 
                        viewModel.reset()
                        currentScreen = "upload_form" 
                    }
                )
                "form_completion" -> FormCompletionScreen(
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onFillAnotherClick = { 
                        viewModel.reset()
                        currentScreen = "upload_form" 
                    },
                    onBackToHomeClick = { currentScreen = "dashboard" }
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
                    onVideoTutorialClick = { currentScreen = "video_tutorial" }
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
                    onNavigateToVoiceSettings = {
                        currentScreen = "voice_selection"
                    }
                )
                "voice_selection" -> VoiceSpeedSelectionScreen(
                    currentSpeed = viewModel.voiceSpeed,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onBackClick = { currentScreen = "settings" },
                    onSpeedSelect = { speed ->
                        viewModel.updateVoiceSpeed(speed)
                        currentScreen = "voice_detail"
                    }
                )
                "voice_detail" -> VoiceGuidanceDetailScreen(
                    speed = viewModel.voiceSpeed,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    viewModel = viewModel,
                    onBackClick = { currentScreen = "voice_selection" }
                )
                "profile" -> ProfileScreen(
                    userName = userName,
                    userEmail = userEmail,
                    userPhone = userPhone,
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onEditProfileClick = { currentScreen = "edit_profile" },
                    onChangePasswordClick = { currentScreen = "change_password" },
                    onLogoutClick = { currentScreen = "login" },
                    onHomeClick = { currentScreen = "dashboard" },
                    onFormsClick = { currentScreen = "forms" },
                    onHelpClick = { currentScreen = "how_to_use" },
                    onSettingsClick = { currentScreen = "settings" },
                    onCameraIconClick = { showUpdatePhotoPopup = true }
                )
                "crop_photo" -> CropPhotoScreen(
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onCancel = { currentScreen = "profile" },
                    onSave = { currentScreen = "photo_updated" }
                )
                "photo_updated" -> ProfilePhotoUpdatedScreen(
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    userName = userName,
                    onGoToProfile = { currentScreen = "profile" }
                )
                "edit_profile" -> EditProfileScreen(
                    currentName = userName,
                    currentEmail = userEmail,
                    currentPhone = userPhone,
                    selectedLanguage = selectedLanguage,
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onBackClick = { currentScreen = "profile" },
                    onSaveClick = { newName, newEmail, newPhone ->
                        userName = newName
                        userEmail = newEmail
                        userPhone = newPhone
                        currentScreen = "profile"
                    }
                )
                "reset_password" -> ResetPasswordScreen(
                    selectedLanguage = selectedLanguage,
                    onResetPasswordClick = { _, _ -> currentScreen = "login" },
                    onBackToLogin = { currentScreen = "login" }
                )
                "change_password" -> ResetPasswordScreen(
                    selectedLanguage = selectedLanguage,
                    onResetPasswordClick = { _, _ -> currentScreen = "profile" },
                    onBackToLogin = { currentScreen = "profile" }
                )
            }

            if (showUpdatePhotoPopup) {
                UpdateProfilePhotoPopup(
                    isDarkMode = isDarkMode,
                    isHighContrast = isHighContrast,
                    onDismiss = { showUpdatePhotoPopup = false },
                    onTakePhoto = { 
                        showUpdatePhotoPopup = false
                        currentScreen = "crop_photo" 
                    },
                    onUploadPhoto = { 
                        showUpdatePhotoPopup = false
                        currentScreen = "crop_photo" 
                    }
                )
            }
        }
    }
}
