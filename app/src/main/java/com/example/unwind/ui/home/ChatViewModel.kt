package com.example.unwind.ui.home

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.unwind.model.Message
import com.example.unwind.network.OpenAiService
import com.example.unwind.model.ChatRequest
import kotlinx.coroutines.launch
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow

class ChatViewModel(private val openAiService: OpenAiService) : ViewModel() {
    private val _messages = MutableStateFlow<List<Message>>(emptyList())
    val messages = _messages.asStateFlow()

    private fun addMessage(newMessage: Message) {
        val currentList = _messages.value.toMutableList()
        currentList.add(newMessage) // Add the new message
        _messages.value = currentList // Update the live data which should trigger the UI update
    }

    fun sendMessage(text: String) {
        val trimmedText = text.trim()
        if (trimmedText.isNotEmpty()) {
            val userMessage = Message(trimmedText, System.currentTimeMillis(), true)
            addMessage(userMessage)

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
                        addMessage(botMessage)
                    }
                } catch (e: Exception) {
                    val errorMessage = "Failed to send message: ${e.message}"
                    addMessage(Message(errorMessage, System.currentTimeMillis(), false))
                    Log.e("ChatViewModel", errorMessage, e)
                }
            }
        }
    }
}
