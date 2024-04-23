package com.example.unwind.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import com.example.unwind.api.QuotesApiService

object QuoteApiRetrofit {
    private const val BASE_URL = "https://api.quotable.io"

    val instance: QuotesApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(QuotesApiService::class.java)
    }
}
