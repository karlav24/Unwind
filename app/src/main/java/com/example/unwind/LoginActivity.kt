package com.example.unwind

import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignUp: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSignUp = findViewById(R.id.buttonSignUp)

        buttonSignUp.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            // Validate the input fields
            if (validateInput(name, email, password)) {
                // Perform your login/signup logic here
                // For now, let's just move to another activity
                moveToNextActivity()
            }
        }
    }

    private fun validateInput(name: String, email: String, password: String): Boolean {
        // Here, you can add your validation logic
        // For example, checking if the fields are not empty
        if (name.isEmpty()) {
            editTextName.error = "Name is required"
            return false
        }

        if (email.isEmpty()) {
            editTextEmail.error = "Email is required"
            return false
        }

        if (password.isEmpty()) {
            editTextPassword.error = "Password is required"
            return false
        }

        return true
    }

    private fun moveToNextActivity() {
        // Move to the next activity
        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish() // Optional: Close the current activity from the stack
    }

}
