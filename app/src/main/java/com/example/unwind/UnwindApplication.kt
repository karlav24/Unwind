package com.example.unwind

import android.app.Application
import com.example.unwind.network.OpenAiService
import com.example.unwind.utils.Config
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UnwindApplication : Application() {
    private lateinit var openAiService: OpenAiService
        private set

    override fun onCreate() {
        super.onCreate()
        Config.initialize(this)
        openAiService = createOpenAiService()
    }

    private fun createOpenAiService(): OpenAiService {
        val loggingInterceptor = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }

        val client = OkHttpClient.Builder()
            .addInterceptor(loggingInterceptor)
            .addInterceptor { chain ->
                val request = chain.request().newBuilder()
                    .addHeader("Authorization", "Bearer ${Config.openAiSecretKey}")
                    .build()
                chain.proceed(request)
            }
            .build()

        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        return retrofit.create(OpenAiService::class.java)
    }
}
