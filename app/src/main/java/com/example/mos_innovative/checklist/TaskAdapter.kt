package com.example.mos_innovative.checklist


import android.app.AlertDialog
import android.content.Context
import android.text.InputType
import android.text.SpannableString
import android.text.style.StrikethroughSpan
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.mos_innovative.R

/**
 * RecyclerView Adapter for displaying checklist tasks.
 */
class TaskAdapter(
    private val context: Context,
    private val listener: Listener
) : RecyclerView.Adapter<TaskAdapter.VH>() {

    // Interface for handling task actions (done/edit/delete)
    interface Listener {
        fun onToggleDone(task: Task, done: Boolean)
        fun onEdit(task: Task, newTitle: String)
        fun onDelete(task: Task)
    }

    // Data source
    private val data: MutableList<Task> = mutableListOf()

    // Replace current list with new one
    fun setItems(list: List<Task>?) {
        data.clear()
        list?.let { data.addAll(it) }
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.activity_item_task, parent, false)
        return VH(view)
    }

    override fun onBindViewHolder(holder: VH, position: Int) {
        val task = data[position]

        // Prevent old listeners from triggering while recycling
        holder.cbDone.setOnCheckedChangeListener(null)
        holder.cbDone.isChecked = task.done

        // Strike-through text if task is done
        holder.tvTitle.text = if (task.done) {
            SpannableString(task.title).apply {
                setSpan(StrikethroughSpan(), 0, length, 0)
            }
        } else {
            task.title
        }

        // Toggle Done
        holder.cbDone.setOnCheckedChangeListener { _, isChecked ->
            listener.onToggleDone(task, isChecked)
        }

        // Edit task
        holder.btnEdit.setOnClickListener { showEditDialog(task) }

        // Delete task
        holder.btnDelete.setOnClickListener { listener.onDelete(task) }
    }

    override fun getItemCount(): Int = data.size

    /**
     * Shows a dialog for editing task title.
     */
    private fun showEditDialog(task: Task) {
        val input = EditText(context).apply {
            inputType = InputType.TYPE_CLASS_TEXT
            setText(task.title)
            setSelection(text.length)
        }

        AlertDialog.Builder(context)
            .setTitle("Edit Task")
            .setView(input)
            .setPositiveButton("Save") { _, _ ->
                val newTitle = input.text.toString().trim()
                if (newTitle.isNotEmpty()) {
                    listener.onEdit(task, newTitle)
                }
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // ViewHolder for one task row
    class VH(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val cbDone: CheckBox = itemView.findViewById(R.id.cbDone)
        val tvTitle: TextView = itemView.findViewById(R.id.tvTitle)
        val btnEdit: ImageButton = itemView.findViewById(R.id.btnEdit)
        val btnDelete: ImageButton = itemView.findViewById(R.id.btnDelete)
    }
}
