package com.example.mos_innovative.MainActivity

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import com.example.mos_innovative.R
import com.example.mos_innovative.ui.auth.LoginActivity
import com.example.mos_innovative.ui.auth.SignUpActivity

/**
 * MainActivity is the entry screen of the app.
 * It provides navigation to either the Login or SignUp screens.
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main) // Inflate the layout for MainActivity

        // Find UI buttons from layout
        val buttonLogin: Button = findViewById(R.id.buttonLogin)   // Login button
        val buttonSignUp: Button = findViewById(R.id.buttonSignUp) // Sign-up button

        // Navigate to LoginActivity when login button is clicked
        buttonLogin.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }

        // Navigate to SignUpActivity when sign-up button is clicked
        buttonSignUp.setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}
