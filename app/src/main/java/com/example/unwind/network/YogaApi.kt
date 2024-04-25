package com.example.unwind.network

import com.example.unwind.api.YogaApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object YogaApi {
    private const val BASE_URL = "https://yoga-api-nzy4.onrender.com"
    val instance: YogaApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(YogaApiService::class.java)
    }
}
