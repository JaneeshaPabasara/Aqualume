package com.example.aqualumeapp.models

data class MeditationLog(
    val id: String,
    val duration: Int,
    val timestamp: Long,
    val timeString: String
)