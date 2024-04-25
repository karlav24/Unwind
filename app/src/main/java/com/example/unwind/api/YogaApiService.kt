package com.example.unwind.api
import retrofit2.Call
import retrofit2.http.*
import com.example.unwind.model.Yoga
import com.example.unwind.model.YogaPose

interface YogaApiService {
    @GET("/v1/categories")
    fun getCategories(): Call<Yoga>

    @GET("/v1/poses")
    fun getPoses(): Call<List<YogaPose>>
}