package com.example.unwind.ui
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.unwind.PaymentActivity
import com.example.unwind.R

class SettingsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.settings)

        val backButton = findViewById<View>(R.id.back_button)
        backButton.setOnClickListener {
            val intent = Intent(this, HomeActivity::class.java)
            startActivity(intent)
        }

        val logout = findViewById<View>(R.id.logout)
        logout.setOnClickListener {
            Log.d("SettingsActivity", "Logoutbutton clicked")
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        val premium = findViewById<TextView>(R.id.unwind_prem)
        premium.setOnClickListener {
            Log.d("SettingsActivity", "Premium button clicked")
            val intent = Intent(this, PremiumActivity::class.java)
            startActivity(intent)
        }

    }

}
