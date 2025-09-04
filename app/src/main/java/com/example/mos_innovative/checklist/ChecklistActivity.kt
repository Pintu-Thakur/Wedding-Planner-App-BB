package com.example.mos_innovative.checklist

import android.app.AlertDialog
import android.os.Bundle
import android.text.InputType
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.mos_innovative.R
import com.google.android.material.floatingactionbutton.ExtendedFloatingActionButton

/**
 * ChecklistActivity
 *
 * This screen shows a wedding planning checklist.
 * Users can:
 *  - Add tasks (via Floating Action Button)
 *  - Mark tasks as done/undone
 *  - Edit task titles
 *  - Delete tasks
 *
 * Uses TaskRepository (data source) + TaskAdapter (RecyclerView).
 */
class ChecklistActivity : AppCompatActivity(), TaskAdapter.Listener {

    // Repository for handling task storage
    private lateinit var repo: TaskRepository

    // RecyclerView adapter for displaying tasks
    private lateinit var adapter: TaskAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_checklist)

        // Initialize repository and adapter
        repo = TaskRepository(this)
        adapter = TaskAdapter(this, this)

        // RecyclerView for showing the checklist
        val rv = findViewById<RecyclerView>(R.id.rvTasks)
        rv.adapter = adapter

        // Floating button to add new tasks
        val fabAdd = findViewById<ExtendedFloatingActionButton>(R.id.fabAdd)
        fabAdd.setOnClickListener { showAddDialog() }

        // Load saved tasks on screen
        loadData()
    }

    /**
     * Load all tasks from repository and update RecyclerView
     */
    private fun loadData() {
        val tasks = repo.getAll()
        adapter.setItems(tasks)
    }

    /**
     * Show a dialog for adding a new task
     */
    private fun showAddDialog() {
        val input = EditText(this).apply {
            hint = "e.g., Book decorators"
            inputType = InputType.TYPE_CLASS_TEXT
        }

        AlertDialog.Builder(this)
            .setTitle("New Task")
            .setView(input)
            .setPositiveButton("Add") { _, _ ->
                val title = input.text.toString().trim()

                // Validate title
                if (title.isEmpty()) {
                    Toast.makeText(this, "Title required", Toast.LENGTH_SHORT).show()
                    return@setPositiveButton
                }

                // Insert into repository and refresh list
                repo.insert(title)
                loadData()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }

    // ---------------- Adapter.Listener methods ----------------

    /**
     * Called when user checks/unchecks a task
     */
    override fun onToggleDone(t: Task, done: Boolean) {
        repo.updateDone(t.id, done)
        loadData()
    }

    /**
     * Called when user edits a task title
     */
    override fun onEdit(t: Task, newTitle: String) {
        repo.updateTitle(t.id, newTitle)
        loadData()
    }

    /**
     * Called when user deletes a task
     */
    override fun onDelete(t: Task) {
        AlertDialog.Builder(this)
            .setMessage("Delete this task?")
            .setPositiveButton("Delete") { _, _ ->
                repo.delete(t.id)
                loadData()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}
