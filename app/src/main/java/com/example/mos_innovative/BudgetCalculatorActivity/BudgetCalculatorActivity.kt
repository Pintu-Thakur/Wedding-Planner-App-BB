package com.example.mos_innovative.BudgetCalculatorActivity

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.mos_innovative.R

/**
 * BudgetCalculatorActivity
 *
 * A simple budget planner that splits the entered wedding budget
 * into percentages for Venue, Catering, and Décor.
 */
class BudgetCalculatorActivity : AppCompatActivity() {

    // UI components
    private lateinit var etBudget: EditText
    private lateinit var btnCalculate: Button
    private lateinit var tvVenue: TextView
    private lateinit var tvCatering: TextView
    private lateinit var tvDecor: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_budget_calculator)

        // Initialize views from XML
        etBudget = findViewById(R.id.etBudget)
        btnCalculate = findViewById(R.id.btnCalculate)
        tvVenue = findViewById(R.id.tvVenue)
        tvCatering = findViewById(R.id.tvCatering)
        tvDecor = findViewById(R.id.tvDecor)

        // Set button click listener for budget calculation
        btnCalculate.setOnClickListener {
            val budgetStr = etBudget.text.toString()

            // Validation: check if user entered something
            if (budgetStr.isEmpty()) {
                Toast.makeText(this, "Please enter budget", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Convert input to Double
            val totalBudget = budgetStr.toDouble()

            // Split budget into percentages:
            // 40% Venue, 35% Catering, 25% Décor
            val venueCost = totalBudget * 0.40
            val cateringCost = totalBudget * 0.35
            val decorCost = totalBudget * 0.25

            // Update TextViews with formatted results
            tvVenue.text = "Venue: ₹${venueCost.toInt()}"
            tvCatering.text = "Catering: ₹${cateringCost.toInt()}"
            tvDecor.text = "Décor: ₹${decorCost.toInt()}"
        }
    }
}
