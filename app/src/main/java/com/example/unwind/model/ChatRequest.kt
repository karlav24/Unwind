package com.example.unwind.model

data class ChatRequest(
    val model: String = "gpt-3.5-turbo",
    val messages: List<Map<String, String>>,  // Assuming 'messages' is a list of maps, adjust according to API requirements
    val max_tokens: Int,
    val temperature: Double? = null  // Include other parameters as needed
)
