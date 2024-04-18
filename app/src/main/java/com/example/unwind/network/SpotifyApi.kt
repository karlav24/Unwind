package com.example.unwind.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object SpotifyApi {
    private const val BASE_URL = "https://api.spotify.com/"

    val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val service: SpotifyService = retrofit.create(SpotifyService::class.java)
}
