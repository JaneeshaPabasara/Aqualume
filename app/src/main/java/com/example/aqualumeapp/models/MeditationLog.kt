package com.example.aqualumeapp.models

data class MeditationLog(
    val id: Long = System.currentTimeMillis(),
    val duration: Int, // in minutes
    val timestamp: Long = System.currentTimeMillis(),
    val isCompleted: Boolean = false
)