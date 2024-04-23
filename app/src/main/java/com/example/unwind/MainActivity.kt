package com.example.unwind

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.unwind.MeditationTracks.MeditationTrackAdapter
import com.example.unwind.MeditationTracks.MeditationViewModel
import com.example.unwind.ui.LoginActivity

class MainActivity : AppCompatActivity() {

    private lateinit var viewModel: MeditationViewModel
    private lateinit var adapter: MeditationTrackAdapter
    private lateinit var start: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        start = findViewById(R.id.start)
        start.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }
    }
}
