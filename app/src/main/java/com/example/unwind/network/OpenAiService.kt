package com.example.unwind.network

import retrofit2.http.POST
import retrofit2.http.Headers
import retrofit2.http.Body
import com.example.unwind.model.ChatResponse
import com.example.unwind.model.ChatRequest

interface OpenAiService {
    @Headers("Content-Type: application/json")
    @POST("v1/engines/davinci-codex/completions")
    suspend fun createCompletion(@Body request: ChatRequest): ChatResponse
}