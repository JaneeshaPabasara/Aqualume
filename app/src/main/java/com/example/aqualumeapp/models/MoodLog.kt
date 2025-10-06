package com.example.aqualumeapp.models

data class MoodLog(
    val id: Long = System.currentTimeMillis(),
    val mood: String, // "Happy", "Sad", "Angry", "Calm", etc.
    val emoji: String, // emoji character
    val timestamp: Long = System.currentTimeMillis()
)