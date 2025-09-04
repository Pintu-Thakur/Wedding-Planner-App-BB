package com.example.mos_innovative.Venue

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mos_innovative.R

/**
 * RecyclerView Adapter for displaying a list of venues.
 *
 * @param venues The list of Venue objects to display in the RecyclerView.
 */
class VenueAdapter(private var venues: List<Venue>) :
    RecyclerView.Adapter<VenueAdapter.VenueViewHolder>() {

    /**
     * ViewHolder class to hold references to the views for each item in the list.
     * This avoids repeatedly calling findViewById during scrolling.
     */
    inner class VenueViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvName)          // Venue name
        val tvLocation: TextView = itemView.findViewById(R.id.tvLocation)  // Venue location
        val tvPrice: TextView = itemView.findViewById(R.id.tvPrice)        // Venue price range
        val tvCapacity: TextView = itemView.findViewById(R.id.tvCapacity)  // Venue capacity
    }

    /**
     * Called when RecyclerView needs a new ViewHolder.
     * Inflates the item layout (item_venue.xml) and returns a VenueViewHolder.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VenueViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_venue, parent, false)
        return VenueViewHolder(view)
    }

    /**
     * Binds data from the Venue object to the UI components inside the ViewHolder.
     */
    override fun onBindViewHolder(holder: VenueViewHolder, position: Int) {
        val venue = venues[position]
        holder.tvName.text = venue.name
        holder.tvLocation.text = venue.location
        holder.tvPrice.text = venue.priceRange
        holder.tvCapacity.text = "${venue.capacity} guests"
    }

    /**
     * Returns the total number of items in the dataset.
     */
    override fun getItemCount(): Int = venues.size

    /**
     * Updates the RecyclerView with a new list of venues and refreshes the UI.
     *
     * @param newList The new list of Venue objects.
     */
    fun updateList(newList: List<Venue>) {
        venues = newList
        notifyDataSetChanged()
    }
}
