package com.example.mos_innovative.Venue

/**
 * Data class representing a wedding venue.
 *
 * @property name The name of the venue.
 * @property location The location/address of the venue.
 * @property priceRange The price range (e.g., "₹50,000 - ₹1,00,000").
 * @property capacity The maximum number of guests the venue can accommodate.
 */
data class Venue(
    val name: String,
    val location: String,
    val priceRange: String,
    val capacity: Int
)
