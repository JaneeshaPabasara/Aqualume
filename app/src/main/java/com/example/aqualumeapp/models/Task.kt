package com.example.aqualumeapp.models

data class Task(
    val id: String,
    val title: String,
    val isCompleted: Boolean,
    val type: TaskType
)
enum class TaskType {
    WATER, MEDITATION, JOURNAL, MEDICINE
}