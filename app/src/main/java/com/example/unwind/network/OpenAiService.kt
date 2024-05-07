package com.example.unwind.network

import retrofit2.http.POST
import retrofit2.http.Headers
import retrofit2.http.Body
import retrofit2.Call
import com.example.unwind.model.ChatResponse
import com.example.unwind.model.ChatRequest

interface OpenAiService {
    @Headers("Content-Type: application/json", "Authorization: Bearer YOUR_API_KEY_HERE")
    @POST("v1/engines/davinci-codex/completions")
    suspend fun createCompletion(@Body request: ChatRequest): ChatResponse
}