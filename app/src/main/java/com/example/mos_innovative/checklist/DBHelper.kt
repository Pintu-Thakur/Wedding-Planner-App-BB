package com.example.mos_innovative.checklist

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper

/**
 * DBHelper
 *
 * This class manages the SQLite database for the wedding checklist.
 * - Creates the tasks table
 * - Seeds the database with some initial checklist items
 * - Handles upgrades (drops old table if schema changes)
 */
class DBHelper(context: Context) : SQLiteOpenHelper(
    context,
    DB_NAME,
    null,
    DB_VERSION
) {
    companion object {
        const val DB_NAME = "wedding_checklist.db"
        const val DB_VERSION = 1

        const val TABLE_TASKS = "tasks"
        const val COL_ID = "_id"
        const val COL_TITLE = "title"
        const val COL_DONE = "done"
    }

    override fun onCreate(db: SQLiteDatabase) {
        // Create tasks table
        db.execSQL(
            """
            CREATE TABLE $TABLE_TASKS (
                $COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,
                $COL_TITLE TEXT NOT NULL,
                $COL_DONE INTEGER DEFAULT 0
            );
            """.trimIndent()
        )

        // Seed checklist with default tasks
        db.execSQL(
            """
            INSERT INTO $TABLE_TASKS ($COL_TITLE) VALUES
                ('Venue booking'),
                ('Photography'),
                ('Catering'),
                ('Mehendi arrangements'),
                ('Sangeet planning'),
                ('Honeymoon booking');
            """.trimIndent()
        )
    }

    override fun onUpgrade(db: SQLiteDatabase, oldVersion: Int, newVersion: Int) {
        // Drop and recreate table when upgrading schema
        db.execSQL("DROP TABLE IF EXISTS $TABLE_TASKS")
        onCreate(db)
    }
}
