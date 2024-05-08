package com.example.unwind.network

import retrofit2.http.Body
import retrofit2.http.Headers
import retrofit2.http.POST
import com.example.unwind.model.ChatRequest
import com.example.unwind.model.ChatResponse

interface OpenAiService {
    @Headers("Content-Type: application/json")
    @POST("v1/engines/gpt-3.5-turbo/completions")
    suspend fun createCompletion(@Body request: ChatRequest): ChatResponse
}
