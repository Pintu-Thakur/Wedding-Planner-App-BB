package com.example.mos_innovative.checklist


import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase

/**
 * Repository class for managing tasks in the SQLite database.
 */
class TaskRepository(context: Context) {

    private val helper = DBHelper(context.applicationContext)

    /**
     * Get all tasks ordered by ID ASC
     */
    fun getAll(): List<Task> {
        val db: SQLiteDatabase = helper.readableDatabase
        val cursor = db.query(
            DBHelper.TABLE_TASKS,
            null, null, null, null, null,
            DBHelper.COL_ID + " ASC"
        )

        val list = mutableListOf<Task>()
        while (cursor.moveToNext()) {
            val id = cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.COL_ID))
            val title = cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COL_TITLE))
            val done = cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COL_DONE)) == 1
            list.add(Task(id, title, done))
        }

        cursor.close()
        db.close()
        return list
    }

    /**
     * Insert a new task
     */
    fun insert(title: String): Long {
        val db: SQLiteDatabase = helper.writableDatabase
        val cv = ContentValues().apply {
            put(DBHelper.COL_TITLE, title)
        }
        val id = db.insert(DBHelper.TABLE_TASKS, null, cv)
        db.close()
        return id
    }

    /**
     * Update task title
     */
    fun updateTitle(id: Long, newTitle: String) {
        val db: SQLiteDatabase = helper.writableDatabase
        val cv = ContentValues().apply {
            put(DBHelper.COL_TITLE, newTitle)
        }
        db.update(DBHelper.TABLE_TASKS, cv, "${DBHelper.COL_ID}=?", arrayOf(id.toString()))
        db.close()
    }

    /**
     * Update task completion status
     */
    fun updateDone(id: Long, done: Boolean) {
        val db: SQLiteDatabase = helper.writableDatabase
        val cv = ContentValues().apply {
            put(DBHelper.COL_DONE, if (done) 1 else 0)
        }
        db.update(DBHelper.TABLE_TASKS, cv, "${DBHelper.COL_ID}=?", arrayOf(id.toString()))
        db.close()
    }

    /**
     * Delete a task by ID
     */
    fun delete(id: Long) {
        val db: SQLiteDatabase = helper.writableDatabase
        db.delete(DBHelper.TABLE_TASKS, "${DBHelper.COL_ID}=?", arrayOf(id.toString()))
        db.close()
    }
}
