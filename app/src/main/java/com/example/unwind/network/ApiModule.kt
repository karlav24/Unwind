package com.example.unwind.network

import android.util.Log
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.unwind.utils.Config

object ApiModule {
    private val authInterceptor = Interceptor { chain ->
        Log.d("API Request", "Interceptor hit")
        val originalRequest = chain.request()
        val newRequest = originalRequest.newBuilder()
            .header("Authorization", "Bearer ${Config.openAiSecretKey}")
            .header("Content-Type", "application/json")
            .method(originalRequest.method, originalRequest.body)
            .build()

        Log.d("API Request", "Requesting: " + newRequest.url)
        chain.proceed(newRequest)
    }

    private val detailedLoggingInterceptor = Interceptor { chain ->
        val request = chain.request()
        val response = chain.proceed(request)
        val responseBody = response.peekBody(Long.MAX_VALUE)

        Log.d("API Request Detailed", "URL: ${request.url}")
        Log.d("API Request Detailed", "Method: ${request.method}")
        Log.d("API Request Detailed", "Request Body: ${request.body}")
        Log.d("API Request Detailed", "Response Code: ${response.code}")
        Log.d("API Request Detailed", "Response Headers: ${response.headers}")
        Log.d("API Request Detailed", "Response Body: ${responseBody.string()}")

        response
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
        .addInterceptor(detailedLoggingInterceptor)
        .build()

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl("https://api.openai.com/")
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val openAiService: OpenAiService by lazy {
        retrofit.create(OpenAiService::class.java)
    }
}
