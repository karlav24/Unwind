package com.example.unwind.ui.home

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.unwind.UnwindApplication
import com.example.unwind.databinding.FragmentChatBinding
import com.example.unwind.model.Message
import com.example.unwind.ui.chat.ChatAdapter
import com.example.unwind.ui.home.ChatViewModel
import com.example.unwind.ui.home.ViewModelFactory
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class ChatFragment : Fragment() {
    private var _binding: FragmentChatBinding? = null
    private val binding get() = _binding!!
    private val chatViewModel: ChatViewModel by viewModels { ViewModelFactory() }
    private lateinit var chatAdapter: ChatAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d("ChatFragment", "onCreateView called")
        _binding = FragmentChatBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
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

    private fun setupRecyclerView() {
        chatAdapter = ChatAdapter(mutableListOf())
        binding.chatRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = chatAdapter
        }
        Log.d("ChatFragment", "RecyclerView set up")
    }

    private fun observeMessages() {
        lifecycleScope.launch {
            chatViewModel.messages.collect { messages ->
                Log.d("ChatFragment", "Messages observed: ${messages.size}")
                chatAdapter.updateData(messages)
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        Log.d("ChatFragment", "onDestroyView called and binding cleared")
    }
}
