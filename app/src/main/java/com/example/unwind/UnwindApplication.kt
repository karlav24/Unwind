package com.example.unwind

import android.app.Application
import com.example.unwind.network.OpenAiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class UnwindApplication : Application() {
    lateinit var openAiService: OpenAiService

    override fun onCreate() {
        super.onCreate()
        openAiService = createOpenAiService()
    }

    private fun createOpenAiService(): OpenAiService {
        return Retrofit.Builder()
            .baseUrl("https://api.openai.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(OpenAiService::class.java)
    }
}
