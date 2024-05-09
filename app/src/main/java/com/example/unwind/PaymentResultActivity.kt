package com.example.unwind
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.unwind.databinding.ActivityPaymentResultBinding

class PaymentResultActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPaymentResultBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPaymentResultBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Retrieve payment result from intent extras
        val paymentSuccessful = intent.getBooleanExtra("paymentSuccessful", false)

        // Display appropriate message based on payment result
        if (paymentSuccessful) {
            binding.textViewResult.text = "Payment Successful"
        } else {
            binding.textViewResult.text = "Payment Failed"
        }

        // Example of a button to navigate back to the main screen
        binding.buttonReturn.setOnClickListener {
            finish() // Close this activity and return to previous activity
        }
    }
}

