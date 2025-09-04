package com.example.mos_innovative.GuestListActivity

import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.mos_innovative.R

/**
 * Activity for managing the Guest List.
 * Users can add guest names and their RSVP status.
 */
class GuestListActivity : AppCompatActivity() {

    // UI Components
    private lateinit var etGuestName: EditText
    private lateinit var spinnerRSVP: Spinner
    private lateinit var btnAddGuest: Button
    private lateinit var recyclerGuests: RecyclerView

    // RecyclerView Adapter
    private lateinit var adapter: GuestAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_guest_list)

        // Initialize UI elements
        etGuestName = findViewById(R.id.etGuestName)       // Input field for guest name
        spinnerRSVP = findViewById(R.id.spinnerRSVP)       // Dropdown for RSVP status
        btnAddGuest = findViewById(R.id.btnAddGuest)       // Button to add guest
        recyclerGuests = findViewById(R.id.recyclerGuests) // RecyclerView to display guests

        // Define RSVP options
        val rsvpOptions = arrayOf("Pending", "Attending", "Not Attending")

        // Set up spinner with RSVP options
        spinnerRSVP.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            rsvpOptions
        )

        // Initialize RecyclerView with empty list
        adapter = GuestAdapter(mutableListOf())
        recyclerGuests.layoutManager = LinearLayoutManager(this)
        recyclerGuests.adapter = adapter

        // Handle Add Guest button click
        btnAddGuest.setOnClickListener {
            val name = etGuestName.text.toString().trim()   // Get guest name
            val rsvp = spinnerRSVP.selectedItem.toString()  // Get selected RSVP status

            if (name.isEmpty()) {
                // Show error if guest name is missing
                Toast.makeText(this, "Enter guest name", Toast.LENGTH_SHORT).show()
            } else {
                // Create Guest object and add to RecyclerView
                val guest = Guest(name, rsvp)
                adapter.addGuest(guest)

                // Clear input fields after adding
                etGuestName.text.clear()
                spinnerRSVP.setSelection(0) // Reset spinner to "Pending"
            }
        }
    }
}
