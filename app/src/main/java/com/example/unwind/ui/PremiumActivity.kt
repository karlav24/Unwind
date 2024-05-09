package com.example.unwind.ui

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.unwind.PaymentActivity
import com.example.unwind.R
class PremiumActivity: AppCompatActivity() {
    private lateinit var premBuy : View
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.premium_activity)
        premBuy = findViewById(R.id.rectangle_prem_buy)
        val backButton = findViewById<View>(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }
        premBuy.setOnClickListener{
            val intent = Intent(this, PaymentActivity::class.java)
            startActivity(intent)
        }

    }
}

