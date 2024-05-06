package com.example.unwind.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.unwind.R
import com.example.unwind.SignUpActivity
import com.example.unwind.user.User
import com.example.unwind.user.UserDao
import com.example.unwind.user.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LoginActivity : AppCompatActivity() {

    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignIn: Button
    private lateinit var buttonSignUpRedirect: Button
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.login_activity)

        userDao = UserDatabase.getDatabase(this).userDao()

        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSignIn = findViewById(R.id.buttonSignIn)
        buttonSignUpRedirect = findViewById(R.id.buttonSignUpRedirect)

        buttonSignIn.setOnClickListener {
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            // Validate the input fields
            if (validateInput(email, password)) {
                // Use coroutines to perform login operation asynchronously
                CoroutineScope(Dispatchers.Main).launch {
                    signIn(email, password)
                }
            }
        }

        buttonSignUpRedirect.setOnClickListener {
            startSignUp()
        }
    }

    private fun validateInput(email: String, password: String): Boolean {
        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Email and password are required", Toast.LENGTH_SHORT).show()
            return false
        }
        return true
    }

    private suspend fun signIn(email: String, password: String) {
        // Check if a user with the provided email and password exists in the database
        val user = userDao.getUserByEmailAndPassword(email, password)
        if (user != null) {
            // User with the provided credentials exists, login successful
            // You can navigate to the next screen or perform any other desired actions
            moveToHomeActivity(user.name)
        } else {
            // No user found with the provided credentials, display an error message
            Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
        }
    }

    private fun moveToHomeActivity(userName: String) {
        val intent = Intent(this, HomeActivity::class.java)
        intent.putExtra("userName", userName)
        startActivity(intent)
        finish()
    }

    private fun startSignUp() {
        val intent = Intent(this, SignUpActivity::class.java)
        startActivity(intent)
        finish()
    }
}

