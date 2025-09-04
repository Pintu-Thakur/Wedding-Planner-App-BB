package com.example.mos_innovative.GuestListActivity

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mos_innovative.R

/**
 * RecyclerView Adapter for displaying a list of Guests.
 *
 * @param guests A mutable list of Guest objects.
 */
class GuestAdapter(private val guests: MutableList<Guest>) :
    RecyclerView.Adapter<GuestAdapter.GuestViewHolder>() {

    /**
     * ViewHolder class to hold and recycle views for each Guest item.
     */
    inner class GuestViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val tvName: TextView = itemView.findViewById(R.id.tvGuestName) // Guest's name
        val tvRSVP: TextView = itemView.findViewById(R.id.tvRSVP)     // Guest's RSVP status
    }

    /**
     * Called when RecyclerView needs a new ViewHolder.
     * Inflates the layout for a single guest item.
     */
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GuestViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_guest, parent, false)
        return GuestViewHolder(view)
    }

    /**
     * Binds guest data (name + RSVP status) to the ViewHolder.
     */
    override fun onBindViewHolder(holder: GuestViewHolder, position: Int) {
        val guest = guests[position]
        holder.tvName.text = guest.name
        holder.tvRSVP.text = "RSVP: ${guest.rsvpStatus}"
    }

    /**
     * Returns the total number of guests.
     */
    override fun getItemCount(): Int = guests.size

    /**
     * Adds a new guest to the list and updates the RecyclerView.
     */
    fun addGuest(guest: Guest) {
        guests.add(guest)
        notifyItemInserted(guests.size - 1) // Notify RecyclerView of new item
    }
}
