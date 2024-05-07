package com.example.unwind.network

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.unwind.utils.Config
object ApiModule {
    private val authInterceptor = Interceptor { chain ->
        val newRequest = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer ${Config.openAiSecretKey}")
            .build()
        chain.proceed(newRequest)
    }

    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(authInterceptor)
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
