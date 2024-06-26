package com.example.unwind.ui.home

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unwind.databinding.ActivityChatBinding
import com.example.unwind.ui.chat.ChatAdapter
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

        // Start the initial conversation
        chatViewModel.startConversation()

        binding.sendButton.setOnClickListener {
            val text = binding.messageEditText.text.toString()
            if (text.isNotEmpty()) {
                chatViewModel.sendMessage(text)
                binding.messageEditText.text = null
            }
        }
    }

    private fun initializeViewModel() {
        val factory = ViewModelFactory()
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
            chatViewModel.messages.collect { newMessages ->
                // Check if there are new messages to add
                if (chatAdapter.itemCount < newMessages.size) {
                    for (index in chatAdapter.itemCount until newMessages.size) {
                        chatAdapter.updateData(newMessages[index])
                    }
                    scrollToBottom()
                }
            }
        }
    }

    private fun scrollToBottom() {
        if (chatAdapter.itemCount > 0) {
            binding.chatRecyclerView.smoothScrollToPosition(chatAdapter.itemCount - 1)
        }
    }
    fun onBackPressed(view: View?) {
        super.onBackPressed()
    }

}
