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

/**
 * SignUpActivity
 * Handles new user registration by saving data into SharedPreferences.
 * After successful signup, user is redirected to LoginActivity.
 */
class SignUpActivity : AppCompatActivity() {

    // UI elements
    private lateinit var editTextUsername: EditText
    private lateinit var editTextEmail: EditText
    private lateinit var editTextPassword: EditText
    private lateinit var buttonSignUp: Button
    private lateinit var textViewLogin: TextView

    // SharedPreferences file name to store user data
    private val PREFS_NAME = "UserPrefs"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup) // Set layout for signup screen

        // Initialize UI elements
        editTextUsername = findViewById(R.id.editTextUsername)
        editTextEmail = findViewById(R.id.editTextEmail)
        editTextPassword = findViewById(R.id.editTextPassword)
        buttonSignUp = findViewById(R.id.buttonSignUp)
        textViewLogin = findViewById(R.id.textViewLogin)

        // Handle SignUp button click
        buttonSignUp.setOnClickListener {
            val username = editTextUsername.text.toString().trim() // Get entered username
            val email = editTextEmail.text.toString().trim()       // Get entered email
            val password = editTextPassword.text.toString().trim() // Get entered password

            // Validate fields
            if (username.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            } else {
                // Save user data into SharedPreferences
                val sharedPreferences: SharedPreferences = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
                val editor = sharedPreferences.edit()
                editor.putString("username", username)
                editor.putString("email", email)
                editor.putString("password", password)
                editor.apply() // Save changes

                Toast.makeText(this, "Sign up successful!", Toast.LENGTH_SHORT).show()

                // Navigate to LoginActivity after successful signup
                startActivity(Intent(this, LoginActivity::class.java))
                finish() // Close SignUpActivity so user cannot go back
            }
        }

        // Navigate to LoginActivity if user already has an account
        textViewLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }
}
