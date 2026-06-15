package com.simats.formsahayak.logic

import com.google.gson.annotations.SerializedName
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Multipart
import retrofit2.http.Part
import okhttp3.MultipartBody
import okhttp3.RequestBody
import java.util.concurrent.TimeUnit

// Request / Response models:
data class AdminLoginRequest(
    val email: String,
    val password: String
)

data class AdminLoginResponse(
    @SerializedName("access_token") val accessToken: String
)

data class AdminStatsAnalytics(
    @SerializedName("upload_dates") val uploadDates: List<String>,
    @SerializedName("upload_counts") val uploadCounts: List<Int>
)

data class AdminStatsResponse(
    @SerializedName("total_users") val totalUsers: Int,
    @SerializedName("total_uploaded_forms") val totalUploadedForms: Int,
    @SerializedName("total_ocr_scans") val totalOcrScans: Int,
    @SerializedName("total_feedback") val totalFeedback: Int,
    @SerializedName("active_users") val activeUsers: Int,
    val analytics: AdminStatsAnalytics?
)

data class AdminUserItem(
    val name: String,
    val email: String,
    val phone: String?,
    val language: String?
)

data class AdminUsersResponse(
    val users: List<AdminUserItem>
)

data class AdminFormItem(
    val id: Int,
    @SerializedName("file_name") val fileName: String,
    @SerializedName("user_email") val userEmail: String,
    @SerializedName("created_at") val createdAt: String,
    @SerializedName("file_url") val fileUrl: String?,
    @SerializedName("extracted_text") val extractedText: String?,
    @SerializedName("guidance_text") val guidanceText: String?,
    @SerializedName("audio_path") val audioPath: String?,
    @SerializedName("pdf_url") val pdfUrl: String?
)

data class AdminFormsResponse(
    val forms: List<AdminFormItem>
)

data class AdminFeedbackItem(
    val id: Int,
    @SerializedName("user_email") val userEmail: String,
    @SerializedName("app_experience") val appExperience: String,
    @SerializedName("voice_guidance_helpful") val voiceGuidanceHelpful: String,
    @SerializedName("recommend_app") val recommendApp: String,
    val rating: Int? = 5,
    @SerializedName("additional_comments") val additionalComments: String?,
    @SerializedName("created_at") val createdAt: String
)

data class AdminFeedbackResponse(
    val feedback: List<AdminFeedbackItem>
)

interface AdminApiService {
    @POST("api/admin/login")
    suspend fun login(@Body request: AdminLoginRequest): Response<AdminLoginResponse>

    @GET("api/admin/stats")
    suspend fun getStats(@Header("Authorization") authHeader: String): Response<AdminStatsResponse>

    @GET("api/admin/users")
    suspend fun getUsers(
        @Header("Authorization") authHeader: String,
        @Query("query") query: String
    ): Response<AdminUsersResponse>

    @GET("api/admin/forms")
    suspend fun getForms(@Header("Authorization") authHeader: String): Response<AdminFormsResponse>

    @GET("api/admin/feedback")
    suspend fun getFeedback(@Header("Authorization") authHeader: String): Response<AdminFeedbackResponse>

    @Multipart
    @POST("api/admin/developer")
    suspend fun updateDeveloperDetails(
        @Header("Authorization") authHeader: String,
        @Part("name") name: RequestBody,
        @Part("father_name") fatherName: RequestBody,
        @Part("role") role: RequestBody,
        @Part("description") description: RequestBody,
        @Part("email") email: RequestBody,
        @Part("github") github: RequestBody,
        @Part("linkedin") linkedin: RequestBody,
        @Part("portfolio") portfolio: RequestBody,
        @Part profile_image: MultipartBody.Part?
    ): Response<UpdateDeveloperResponse>
}

data class UpdateDeveloperResponse(
    val message: String
)

val adminApiService: AdminApiService by lazy {
    val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }
    val client = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    Retrofit.Builder()
        .baseUrl(RetrofitClient.BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client)
        .build()
        .create(AdminApiService::class.java)
}
