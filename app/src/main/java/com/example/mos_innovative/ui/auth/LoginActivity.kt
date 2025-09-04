package com.example.mos_innovative.ui.auth

import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mos_innovative.R
import com.example.mos_innovative.ui.home.HomeActivity

/**
 * LoginActivity
 * Handles user login using locally stored SharedPreferences.
 * If credentials match, user is navigated to HomeActivity.
 */
class LoginActivity : AppCompatActivity() {

    companion object {
        // Name of SharedPreferences file where user credentials are stored
        private const val PREFS_NAME = "UserPrefs"
    }

    // UI elements
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonLogin: Button
    private lateinit var textViewSignUp: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login) // Set the layout for this activity

        // Initialize UI elements
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonLogin = findViewById(R.id.buttonLogin)
        textViewSignUp = findViewById(R.id.textViewSignUp)

        // Handle login button click
        buttonLogin.setOnClickListener {
            val email = editTextEmail.text.toString().trim()       // Get email input
            val password = editTextPassword.text.toString().trim() // Get password input

            // Access stored credentials from SharedPreferences
            val sharedPreferences: SharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            val storedEmail = sharedPreferences.getString("email", null)
            val storedPassword = sharedPreferences.getString("password", null)

            // Validate input fields
            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Email and password cannot be empty", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Check if entered credentials match stored ones
            if (email == storedEmail && password == storedPassword) {
                Toast.makeText(this, "Login successful!", Toast.LENGTH_SHORT).show()

                // Navigate to HomeActivity and pass the username (email) as extra data
                val intent = Intent(this, HomeActivity::class.java)
                intent.putExtra("USERNAME", storedEmail)
                startActivity(intent)

                // Add transition animation between screens
                overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)

                // Finish LoginActivity so user cannot go back to it using back button
                finish()
            } else {
                // Show error message if credentials don't match
                Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
            }
        }

        // Navigate to SignUpActivity when "Sign Up" text is clicked
        textViewSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}
