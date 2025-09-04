package com.example.mos_innovative.GuestListActivity

/**
 * Guest data model
 *
 * Represents a single guest with:
 * - name: Guest's full name
 * - rsvpStatus: RSVP status (e.g., "Confirmed", "Pending", "Declined")
 */
data class Guest(
    val name: String,        // Guest name
    val rsvpStatus: String   // RSVP status
)
