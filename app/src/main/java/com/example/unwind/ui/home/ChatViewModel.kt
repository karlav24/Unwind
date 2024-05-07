package com.example.unwind.ui.home

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
            _messages.value = _messages.value + Message(text, System.currentTimeMillis(), true)
            viewModelScope.launch {
                try {
                    val response = openAiService.createCompletion(ChatRequest(prompt = trimmedText, max_tokens = 150))
                    val botMessage = response.choices.firstOrNull()?.text?.trim() ?: "Error: Unable to get a response."
                    _messages.value = _messages.value + Message(botMessage, System.currentTimeMillis(), false)
                } catch (e: Exception) {
                    _messages.value = _messages.value + Message("Failed to send message: ${e.message}", System.currentTimeMillis(), false)
                }
            }
        }
    }
}

