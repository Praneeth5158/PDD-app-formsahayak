package com.simats.formsahayak.logic

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {
    const val BASE_URL = "https://formsahayak-backend.onrender.com/"

    private val logging = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .addInterceptor(logging)
        .connectTimeout(60, TimeUnit.SECONDS)
        .readTimeout(60, TimeUnit.SECONDS)
        .writeTimeout(60, TimeUnit.SECONDS)
        .build()

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(httpClient)
            .build()
            .create(ApiService::class.java)
    }
}

object UrlHelper {
    fun cleanUrl(url: String?): String {
        if (url.isNullOrEmpty()) return ""
        var clean = url.trim().replace("\\", "/")
        clean = clean.replace("%5C", "/", ignoreCase = true)
        return if (clean.startsWith("http", ignoreCase = true)) {
            clean
        } else {
            RetrofitClient.BASE_URL.removeSuffix("/") + "/" + clean.removePrefix("/")
        }
    }
}

