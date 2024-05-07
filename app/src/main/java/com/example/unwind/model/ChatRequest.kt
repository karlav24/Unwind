package com.example.unwind.model

data class ChatRequest(
    val prompt: String,
    val max_tokens: Int
)