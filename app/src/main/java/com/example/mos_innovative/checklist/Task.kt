package com.example.mos_innovative.checklist

/**
 * Task model class
 *
 * Represents a single checklist task with:
 * - id (primary key in DB)
 * - title (task name)
 * - done (completion status)
 */
data class Task(
    val id: Long,
    var title: String,
    var done: Boolean
)
