package com.example.unwind.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import com.example.unwind.BuildConfig

object SpotifyApi {
    private const val BASE_URL = "https://api.spotify.com/"

    // Configure the OkHttpClient to add a logging interceptor
    private val client = OkHttpClient.Builder().apply {
        if (BuildConfig.DEBUG) {
            // Add the logging interceptor to the OkHttpClient in debug builds
            val loggingInterceptor = HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }
            addInterceptor(loggingInterceptor)
        }
    }.build()

    // Build the Retrofit instance using the configured OkHttpClient
    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .client(client) // Use the custom OkHttpClient
        .build()

    // Create an instance of the SpotifyService using the Retrofit instance
    val service: SpotifyService = retrofit.create(SpotifyService::class.java)
}
