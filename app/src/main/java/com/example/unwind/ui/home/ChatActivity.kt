package com.example.unwind.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.unwind.databinding.ActivityChatBinding
import com.example.unwind.ui.home.ChatFragment

class ChatActivity : AppCompatActivity() {
    private lateinit var binding: ActivityChatBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityChatBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Load ChatFragment
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(binding.chatContainer.id, ChatFragment())
                .commit()
        }
    }
}
