package com.example.unwind

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class HomeActivity : AppCompatActivity() {
    private lateinit var welcome_back: TextView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.home_page_main)
        welcome_back = findViewById(R.id.welcome_bac)
        val userName = intent.getStringExtra("userName")
        if (userName != null) {
            welcome_back.text = "Welcome back, $userName!"
        } else {
            welcome_back.text = "Welcome back!"
        }
        val quotesFragment = QuotesFragment()
        supportFragmentManager.beginTransaction()
            .replace(R.id.fragmentContainer, quotesFragment)
            .commit()

    }
}