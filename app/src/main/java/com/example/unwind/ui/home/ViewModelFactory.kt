package com.example.unwind.ui.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.unwind.network.OpenAiService

class ViewModelFactory(private val openAiService: OpenAiService) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ChatViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return ChatViewModel(openAiService) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
