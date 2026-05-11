package com.simats.formsahayak.logic

import com.google.gson.annotations.SerializedName
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

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

data class UploadResponse(
    val message: String,
    val guidance: String,
    @SerializedName("audio_file") val audioFile: String
)

interface ApiService {
    @POST("signup")
    suspend fun signup(@Body request: SignupRequest): Response<SignupResponse>

    @POST("login")
    suspend fun login(@Body request: LoginRequest): Response<LoginResponse>

    @Multipart
    @POST("upload-document")
    suspend fun uploadDocument(
        @Part("user_email") userEmail: RequestBody,
        @Part file: MultipartBody.Part
    ): Response<UploadResponse>
}
