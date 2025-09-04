package com.example.mos_innovative.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.mos_innovative.BudgetCalculatorActivity.BudgetCalculatorActivity
import com.example.mos_innovative.GuestListActivity.GuestListActivity
import com.example.mos_innovative.Venue.VenueListActivity
import com.example.mos_innovative.checklist.ChecklistActivity
import com.example.mos_innovative.R

/**
 * HomeActivity
 * Acts as the main dashboard screen after user login.
 * Provides navigation to different modules of the wedding planner app:
 * Checklist, Venue Listing, Guest List, Budget Calculator, and Logout.
 */
class HomeActivity : AppCompatActivity() {

    // Declare UI buttons
    private lateinit var buttonChecklist: Button
    private lateinit var buttonVenueListing: Button
    private lateinit var buttonGuestList: Button
    private lateinit var buttonBudget: Button
    private lateinit var buttonExit: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home) // Load home screen layout

        // Initialize UI elements
        buttonChecklist = findViewById(R.id.buttonChecklist)
        buttonVenueListing = findViewById(R.id.buttonVenueListing)
        buttonGuestList = findViewById(R.id.buttonGuestList)
        buttonBudget = findViewById(R.id.buttonBudget)
        buttonExit = findViewById(R.id.buttonExit)

        // Navigate to Wedding Checklist module
        // Navigate to Wedding Checklist module
        val clickAnim = AnimationUtils.loadAnimation(this, R.anim.button_click)

        buttonChecklist.setOnClickListener {
            it.startAnimation(clickAnim)
            startActivity(Intent(this, ChecklistActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }


// Navigate to Venue Listing module
        buttonVenueListing.setOnClickListener {
            startActivity(Intent(this, VenueListActivity::class.java))
            overridePendingTransition(
                android.R.anim.slide_in_left,
                android.R.anim.slide_out_right
            ) // Slide transition
        }

// Navigate to Guest List module
        buttonGuestList.setOnClickListener {
            startActivity(Intent(this, GuestListActivity::class.java))
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }

// Navigate to Budget Calculator module
        buttonBudget.setOnClickListener {
            startActivity(Intent(this, BudgetCalculatorActivity::class.java))
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out)
        }
        buttonExit.setOnClickListener {
            Toast.makeText(this, "Logging out...", Toast.LENGTH_SHORT).show()
            finish()
            overridePendingTransition(android.R.anim.slide_in_left, android.R.anim.slide_out_right)
        }
    }
}