package com.simats.formsahayak.logic

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Headers
import retrofit2.http.DELETE

data class SignupRequest(
    val name: String,
    val email: String,
    val password: String,
    @SerializedName("confirm_password") val confirmPassword: String
)

data class SignupResponse(
    val message: String
)

data class LoginRequest(
    val email: String,
    val password: String
)

data class UserResponse(
    val name: String,
    val email: String
)

data class LoginResponse(
    val message: String,
    val user: UserResponse
)

data class BackendField(
    val name: String,
    val instruction: String,
    val top: Int,
    val left: Int,
    val right: Int,
    val bottom: Int
)

data class UploadResponse(
    val message: String,
    val guidance: String,
    @SerializedName("audio_file") val audioFile: String,
    @SerializedName("form_type") val formType: String? = null,
    val fields: List<BackendField>? = null,
    @SerializedName("pdf_file") val pdfFile: String? = null
)

data class ProfileResponse(
    val name: String,
    val email: String,
    val phone: String?,
    val language: String?,
    @SerializedName("profile_image") val profileImage: String?
)

data class UpdateProfileResponse(
    val message: String
)

data class ChangePasswordRequest(
    val email: String,
    @SerializedName("new_password") val newPassword: String,
    @SerializedName("confirm_password") val confirmPassword: String
)

data class ChangePasswordResponse(
    val message: String
)

data class SendOtpRequest(
    val email: String
)

data class SendOtpResponse(
    val message: String
)

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)

data class VerifyOtpResponse(
    val message: String
)

data class ResetPasswordRequest(
    val email: String,
    @SerializedName("new_password") val newPassword: String,
    @SerializedName("confirm_password") val confirmPassword: String
)

data class ResetPasswordResponse(
    val message: String
)

data class HistoryItem(
    val id: Int? = null,
    @SerializedName("file_name") val fileName: String? = null,
    @SerializedName("file_url") val fileUrl: String? = null,
    @SerializedName("guidance_text") val guidanceText: String? = null,
    @SerializedName("audio_path") val audioPath: String? = null,
    @SerializedName("pdf_file") val pdfFile: String? = null,
    @SerializedName("pdf_path") val pdfPath: String? = null,
    @SerializedName("pdf_url") val pdfUrl: String? = null,
    @SerializedName("created_at") val createdAt: String? = null
)

data class HistoryResponse(
    val history: List<HistoryItem>
)

data class FeedbackRequest(
    @SerializedName("user_email") val userEmail: String,
    @SerializedName("app_experience") val appExperience: String,
    @SerializedName("voice_guidance_helpful") val voiceGuidanceHelpful: String,
    @SerializedName("recommend_app") val recommendApp: String,
    @SerializedName("additional_comments") val additionalComments: String
)

data class FeedbackResponse(
    val message: String
)

data class FormDetailsResponse(
    val id: Int,
    @SerializedName("file_name") val fileName: String,
    @SerializedName("file_url") val fileUrl: String?,
    @SerializedName("extracted_text") val extractedText: String?,
    @SerializedName("guidance_text") val guidanceText: String?,
    @SerializedName("audio_path") val audioPath: String?,
    @SerializedName("created_at") val createdAt: String?
)

data class DeleteHistoryResponse(
    val message: String
)
interface ApiService {
    @POST("signup")
    suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @GET("profile/{email}")
    suspend fun getProfile(@Path("email") email: String): Response<ProfileResponse>

    @Multipart
    @POST("update-profile")
    suspend fun updateProfile(
        @Part("email") email: RequestBody,
        @Part("phone") phone: RequestBody,
        @Part("language") language: RequestBody,
        @Part profile_image: MultipartBody.Part?
    ): Response<UpdateProfileResponse>

    @Multipart
    @POST("upload-document")
    suspend fun uploadDocument(
        @Part("user_email") userEmail: RequestBody,
        @Part("language") language: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<UploadResponse>

    @GET("history/{email}")
    suspend fun getHistory(@Path("email") email: String): Response<HistoryResponse>

    @POST("submit-feedback")
    suspend fun submitFeedback(@Body request: FeedbackRequest): Response<FeedbackResponse>

    @GET("form-details/{doc_id}")
    suspend fun getFormDetails(@Path("doc_id") docId: Int): Response<FormDetailsResponse>

    @POST("change-password")
    suspend fun changePassword(@Body request: ChangePasswordRequest): Response<ChangePasswordResponse>

    @POST("send-otp")
    suspend fun sendOtp(@Body request: SendOtpRequest): Response<SendOtpResponse>

    @POST("verify-otp")
    suspend fun verifyOtp(@Body request: VerifyOtpRequest): Response<VerifyOtpResponse>

    @POST("reset-password")
    suspend fun resetPassword(@Body request: ResetPasswordRequest): Response<ResetPasswordResponse>

    @DELETE("delete-history/{doc_id}")
    suspend fun deleteHistory(
        @Path("doc_id") docId: Int
    ): Response<DeleteHistoryResponse>

    @GET("api/developer")
    suspend fun getDeveloperDetails(): Response<DeveloperDetailsResponse>
}

data class DeveloperDetailsResponse(
    val name: String?,
    @SerializedName("father_name") val fatherName: String?,
    val role: String?,
    val description: String?,
    val email: String?,
    val github: String?,
    val linkedin: String?,
    val portfolio: String?,
    @SerializedName("profile_image") val profileImage: String?
)
