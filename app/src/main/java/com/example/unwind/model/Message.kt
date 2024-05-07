package com.example.unwind.model

data class Message(
    val text: String,
    val timestamp: Long,
    val isSentByUser: Boolean = true  // This can help differentiate between user messages and bot responses
)
