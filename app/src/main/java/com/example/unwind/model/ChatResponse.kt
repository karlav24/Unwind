package com.example.unwind.model

data class ChatResponse(
    val choices: List<Choice>
)

data class Choice(
    val text: String
)