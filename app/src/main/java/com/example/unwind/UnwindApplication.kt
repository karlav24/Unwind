package com.example.unwind

import android.app.Application
import com.example.unwind.network.OpenAiService
import com.example.unwind.utils.Config
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UnwindApplication : Application() {
    lateinit var openAiService: OpenAiService
        private set

    override fun onCreate() {
        super.onCreate()
        Config.initialize(this)
        openAiService = createOpenAiService()
    }

    private fun createOpenAiService(): OpenAiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .client(
                OkHttpClient.Builder()
                    .addInterceptor { chain ->
                        val request = chain.request().newBuilder()
                            .addHeader("Authorization", "Bearer ${Config.openAiSecretKey}")
                            .build()
                        chain.proceed(request)
                    }
                    .build()
            )
            .build()

        return retrofit.create(OpenAiService::class.java)
    }
}
