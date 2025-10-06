package com.example.aqualumeapp.models

data class UserSettings(
    val waterGoal: Int = 2000, // ml per day
    val meditationGoal: Int = 20, // minutes per day
    val waterReminderInterval: Long = 3600000, // 1 hour in milliseconds
    val meditationReminderEnabled: Boolean = true,
    val journalReminderEnabled: Boolean = true
)