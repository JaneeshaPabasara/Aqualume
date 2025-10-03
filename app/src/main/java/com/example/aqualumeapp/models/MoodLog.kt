package com.example.aqualumeapp.models

data class MoodLog(
    val id: String,
    val mood: String,
    val emoji: String,
    val note: String,
    val timestamp: Long,
    val timeString: String
)