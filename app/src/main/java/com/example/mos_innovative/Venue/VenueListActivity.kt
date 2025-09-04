package com.example.mos_innovative.Venue

import android.os.Bundle
import android.view.animation.AnimationUtils
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mos_innovative.R

/**
 * Activity to display a list of wedding venues with filters for budget and capacity.
 */
class VenueListActivity : AppCompatActivity() {

    // UI elements
    private lateinit var spinnerBudget: Spinner
    private lateinit var spinnerCapacity: Spinner
    private lateinit var btnApply: Button
    private lateinit var recyclerVenues: RecyclerView
    private lateinit var adapter: VenueAdapter

    // List of all venues (static demo data)
    private lateinit var allVenues: List<Venue>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_venues2) // Attach layout XML

        // Initialize UI references
        spinnerBudget = findViewById(R.id.spinnerBudget)
        spinnerCapacity = findViewById(R.id.spinnerCapacity)
        btnApply = findViewById(R.id.btnApply)
        recyclerVenues = findViewById(R.id.recyclerVenues)
        val slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up)
        btnApply.startAnimation(slideUp)

        // Dropdown filter options
        val budgetOptions = arrayOf("Any", "<50k", "50k-1L", "1L-2L", ">2L")
        val capacityOptions = arrayOf("Any", "<100", "100-300", "300-600", ">600")

        // Set adapters for spinners
        spinnerBudget.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, budgetOptions)
        spinnerCapacity.adapter =
            ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, capacityOptions)

        // Static list of demo venues
        allVenues = listOf(
            Venue("Rosewood Banquet", "Delhi", "50k-1L", 120),
            Venue("Lotus Garden", "Gurgaon", "1L-2L", 300),
            Venue("Ocean View Lawn", "Goa", ">2L", 400),
            Venue("Sunset Pavilion", "Jaipur", "50k-1L", 200),
            Venue("Royal Palace Hall", "Udaipur", ">2L", 700),
            Venue("Meadow Greens", "Chandigarh", "<50k", 80),
            Venue("Heritage Courtyard", "Kolkata", "1L-2L", 350),
            Venue("City Lights Rooftop", "Mumbai", "50k-1L", 150)
        )

        // Setup RecyclerView with adapter
        adapter = VenueAdapter(allVenues)
        recyclerVenues.layoutManager = LinearLayoutManager(this)
        recyclerVenues.adapter = adapter
        recyclerVenues.layoutAnimation =
            android.view.animation.AnimationUtils.loadLayoutAnimation(this, R.anim.layout_fall_down)


        btnApply.animate().scaleX(0.95f).scaleY(0.95f).setDuration(100).withEndAction {
            btnApply.animate().scaleX(1f).scaleY(1f).duration = 100
        }

        // Handle Apply Filters button click
        btnApply.setOnClickListener {
            val selectedBudget = spinnerBudget.selectedItem.toString()
            val selectedCapacity = spinnerCapacity.selectedItem.toString()

            // Apply filtering logic
            val filtered = filterVenues(allVenues, selectedBudget, selectedCapacity)
            adapter.updateList(filtered)

            // Show message if no venues match the filters
            if (filtered.isEmpty()) {
                Toast.makeText(this, "No venues found for selected filters", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    /**
     * Filters venues based on selected budget and capacity.
     */
    private fun filterVenues(source: List<Venue>, budgetRange: String, capacityRange: String): List<Venue> {
        val budgetBounds = budgetToBounds(budgetRange) // min and max values for budget
        val capBounds = capacityToBounds(capacityRange) // min and max values for capacity

        return source.filter { venue ->
            // Check if venue matches budget filter
            val budgetOk = if (budgetRange == "Any") true else {
                val venueBounds = budgetToBounds(venue.priceRange)
                val (venueMin, venueMax) = venueBounds
                val (selMin, selMax) = budgetBounds

                if (selMax == -1L) { // Open-ended budget (e.g. >2L)
                    venueMax == -1L || venueMax >= selMin
                } else {
                    venueMin <= selMax && (venueMax == -1L || venueMax >= selMin)
                }
            }

            // Check if venue matches capacity filter
            val capacityOk = if (capacityRange == "Any") true else {
                val vCap = venue.capacity
                val (selMin, selMax) = capBounds
                if (selMax == -1) vCap >= selMin else vCap in selMin..selMax
            }

            // Venue must satisfy both filters
            budgetOk && capacityOk
        }
    }

    /**
     * Maps a budget range string into numerical min and max bounds.
     * Example: "50k-1L" -> 50,001 to 100,000
     */
    private fun budgetToBounds(b: String): Pair<Long, Long> {
        return when (b) {
            "<50k" -> 0L to 50_000L
            "50k-1L" -> 50_001L to 100_000L
            "1L-2L" -> 100_001L to 200_000L
            ">2L" -> 200_001L to -1L // -1 represents "no upper limit"
            else -> 0L to -1L        // "Any"
        }
    }

    /**
     * Maps a capacity range string into numerical min and max bounds.
     * Example: "100-300" -> 100 to 300
     */
    private fun capacityToBounds(c: String): Pair<Int, Int> {
        return when (c) {
            "<100" -> 0 to 99
            "100-300" -> 100 to 300
            "300-600" -> 300 to 600
            ">600" -> 601 to -1  // -1 means "no upper limit"
            else -> 0 to -1      // "Any"
        }
    }
}
