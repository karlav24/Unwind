package com.example.unwind
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.unwind.R
import com.example.unwind.ui.HomeActivity
import com.example.unwind.user.User
import com.example.unwind.user.UserDao
import com.example.unwind.user.UserDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class SignUpActivity : AppCompatActivity() {
    private lateinit var editTextName: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignUp: Button
    private lateinit var userDao: UserDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.sign_up_activity)
        editTextName = findViewById(R.id.editTextName)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSignUp = findViewById(R.id.buttonSignUp)

        userDao = UserDatabase.getDatabase(this).userDao()

        buttonSignUp.setOnClickListener {
            val name = editTextName.text.toString().trim()
            val email = editTextEmail.text.toString().trim()
            val password = editTextPassword.text.toString().trim()

            if (name.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Use coroutines to perform signup operation asynchronously
            CoroutineScope(Dispatchers.Main).launch {
                signUp(name, email, password)
            }
        }
    }

    private suspend fun signUp(name: String, email: String, password: String) {
        // Check if a user with the same email already exists
        val existingUser = userDao.getUserByEmail(email)
        if (existingUser != null) {
            // User with the same email already exists, display a toast
            Toast.makeText(this, "Email already in use", Toast.LENGTH_SHORT).show()
            return
        }

        // No user with the same email exists, proceed with signup
        val newUser = User(name = name, email = email, password = password)
        userDao.insert(newUser)

        // Signup successful, navigate to another screen (e.g., login screen)
        startActivity(Intent(this, HomeActivity::class.java))
        finish() // Finish the current activity to prevent user from navigating back
    }
}
