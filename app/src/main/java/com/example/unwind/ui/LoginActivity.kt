package com.example.unwind.ui

import androidx.appcompat.app.AppCompatActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import com.example.unwind.R

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignUp: Button
    private lateinit var signIn: Button

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
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("userName", editTextName.text)
        startActivity(intent)
        finish()
    }

}
