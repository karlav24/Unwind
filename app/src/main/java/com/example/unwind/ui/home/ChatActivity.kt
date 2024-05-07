package com.example.unwind.ui.home

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unwind.UnwindApplication
import com.example.unwind.databinding.ActivityChatBinding
import com.example.unwind.model.Message
import com.example.unwind.ui.chat.ChatAdapter
import com.example.unwind.ui.home.ChatViewModel
import com.example.unwind.ui.home.ViewModelFactory
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding
    private lateinit var chatViewModel: ChatViewModel
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initializeViewModel()
        setupRecyclerView()
        observeMessages()
        binding.sendButton.setOnClickListener {
            val text = binding.messageEditText.text.toString()
            if (text.isNotEmpty()) {
                chatViewModel.sendMessage(text)
                binding.messageEditText.text = null
            }
        }
    }

    private fun initializeViewModel() {
        val factory = ViewModelFactory((application as UnwindApplication).openAiService)
        chatViewModel = ViewModelProvider(this, factory)[ChatViewModel::class.java]
    }

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(mutableListOf())
        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(this@ChatActivity)
            adapter = chatAdapter
        }
    }

    private fun observeMessages() {
        lifecycleScope.launch {
            chatViewModel.messages.collect { messages ->
                chatAdapter.updateData(messages)
            }
        }
    }
}
