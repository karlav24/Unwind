package com.example.unwind.api
import retrofit2.Call
import retrofit2.http.GET
import com.example.unwind.model.Quote
interface QuotesApiService {
    @GET("/random?maxLength=80")
    fun getRandomQuote(): Call<Quote>
}