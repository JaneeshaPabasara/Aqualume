package com.example.aqualumeapp.models

data class UserSettings(
    val waterGoal: Int = 3000,
    val meditationGoal: Int = 30,
    val waterReminderEnabled: Boolean = false,
    val waterReminderInterval: Int = 2,
    val meditationReminderEnabled: Boolean = false,
    val meditationReminderTime: String = "09:00 AM",
    val journalReminderEnabled: Boolean = false,
    val journalReminderTime: String = "09:00 AM"
)