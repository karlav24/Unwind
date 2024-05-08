package com.example.unwind.ui.home

import android.util.Log
import com.example.unwind.model.Message
import com.example.unwind.network.OpenAiService
import com.example.unwind.model.ChatRequest
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatViewModel(private val openAiService: OpenAiService) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    fun sendMessage(text: String) {
        val trimmedText = text.trim()
        if (trimmedText.isNotEmpty()) {
            // Log the action of sending a message
            Log.d("ChatViewModel", "Sending message: $trimmedText")

            // Create user message and append to current list
            val userMessage = Message(trimmedText, System.currentTimeMillis(), true)
            updateMessages(userMessage)

            // Launch coroutine to handle API response
            viewModelScope.launch {
                try {
                    val chatRequest = ChatRequest(
                        messages = listOf(mapOf("role" to "user", "content" to trimmedText)),
                        max_tokens = 150,
                        model = "gpt-3.5-turbo"
                    )
                    val response = openAiService.createCompletion(chatRequest)
                    response.choices.firstOrNull()?.message?.content?.let {
                        val botMessage = Message(it, System.currentTimeMillis(), false)
                        updateMessages(botMessage)
                    }
                } catch (e: Exception) {
                    // Handle exceptions and log errors
                    val errorMessage = "Failed to send message: ${e.message}"
                    updateMessages(Message(errorMessage, System.currentTimeMillis(), false))
                    Log.e("ChatViewModel", errorMessage, e)
                }
            }
        }
    }

    // Function to safely update message list
    private fun updateMessages(newMessage: Message) {
        val updatedList = _messages.value.toMutableList().apply {
            add(newMessage)
        }
        _messages.value = updatedList
        Log.d("ChatViewModel", "Message added: ${newMessage.text}")
    }
}

