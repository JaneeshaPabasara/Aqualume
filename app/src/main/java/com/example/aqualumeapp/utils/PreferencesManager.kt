package com.example.aqualumeapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.aqualumeapp.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.util.UUID

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences = context.getSharedPreferences("AqualumePrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Water Logs
    fun saveWaterLog(waterLog: WaterLog) {
        val logs = getWaterLogs().toMutableList()
        logs.add(waterLog)
        val json = gson.toJson(logs)
        prefs.edit().putString("water_logs", json).apply()
    }

    fun getWaterLogs(): List<WaterLog> {
        val json = prefs.getString("water_logs", null) ?: return emptyList()
        val type = object : TypeToken<List<WaterLog>>() {}.type
        return gson.fromJson(json, type)
    }

    fun updateWaterLog(updatedLog: WaterLog) {
        val logs = getWaterLogs().toMutableList()
        val index = logs.indexOfFirst { it.id == updatedLog.id }
        if (index != -1) {
            logs[index] = updatedLog
            val json = gson.toJson(logs)
            prefs.edit().putString("water_logs", json).apply()
        }
    }

    fun deleteWaterLog(id: String) {
        val logs = getWaterLogs().toMutableList()
        logs.removeAll { it.id == id }
        val json = gson.toJson(logs)
        prefs.edit().putString("water_logs", json).apply()
    }

    // Mood Logs
    fun saveMoodLog(moodLog: MoodLog) {
        val logs = getMoodLogs().toMutableList()
        logs.add(moodLog)
        val json = gson.toJson(logs)
        prefs.edit().putString("mood_logs", json).apply()
    }

    fun getMoodLogs(): List<MoodLog> {
        val json = prefs.getString("mood_logs", null) ?: return emptyList()
        val type = object : TypeToken<List<MoodLog>>() {}.type
        return gson.fromJson(json, type)
    }

    fun updateMoodLog(updatedLog: MoodLog) {
        val logs = getMoodLogs().toMutableList()
        val index = logs.indexOfFirst { it.id == updatedLog.id }
        if (index != -1) {
            logs[index] = updatedLog
            val json = gson.toJson(logs)
            prefs.edit().putString("mood_logs", json).apply()
        }
    }

    fun deleteMoodLog(id: String) {
        val logs = getMoodLogs().toMutableList()
        logs.removeAll { it.id == id }
        val json = gson.toJson(logs)
        prefs.edit().putString("mood_logs", json).apply()
    }

    // Meditation Logs
    fun saveMeditationLog(meditationLog: MeditationLog) {
        val logs = getMeditationLogs().toMutableList()
        logs.add(meditationLog)
        val json = gson.toJson(logs)
        prefs.edit().putString("meditation_logs", json).apply()
    }

    fun getMeditationLogs(): List<MeditationLog> {
        val json = prefs.getString("meditation_logs", null) ?: return emptyList()
        val type = object : TypeToken<List<MeditationLog>>() {}.type
        return gson.fromJson(json, type)
    }

    fun updateMeditationLog(updatedLog: MeditationLog) {
        val logs = getMeditationLogs().toMutableList()
        val index = logs.indexOfFirst { it.id == updatedLog.id }
        if (index != -1) {
            logs[index] = updatedLog
            val json = gson.toJson(logs)
            prefs.edit().putString("meditation_logs", json).apply()
        }
    }

    fun deleteMeditationLog(id: String) {
        val logs = getMeditationLogs().toMutableList()
        logs.removeAll { it.id == id }
        val json = gson.toJson(logs)
        prefs.edit().putString("meditation_logs", json).apply()
    }

    // Tasks
    fun saveTasks(tasks: List<Task>) {
        val json = gson.toJson(tasks)
        prefs.edit().putString("tasks", json).apply()
    }

    fun getTasks(): List<Task> {
        val json = prefs.getString("tasks", null) ?: return getDefaultTasks()
        val type = object : TypeToken<List<Task>>() {}.type
        return gson.fromJson(json, type)
    }

    private fun getDefaultTasks(): List<Task> {
        return listOf(
            Task(UUID.randomUUID().toString(), "Drink water - 3 liters", false, TaskType.WATER),
            Task(UUID.randomUUID().toString(), "Write the journal", false, TaskType.JOURNAL),
            Task(UUID.randomUUID().toString(), "Meditate 30 min", false, TaskType.MEDITATION),
            Task(UUID.randomUUID().toString(), "Take medicine", false, TaskType.MEDICINE)
        )
    }

    fun updateTask(taskId: String, isCompleted: Boolean) {
        val tasks = getTasks().toMutableList()
        val index = tasks.indexOfFirst { it.id == taskId }
        if (index != -1) {
            tasks[index] = tasks[index].copy(isCompleted = isCompleted)
            saveTasks(tasks)
        }
    }

    // User Settings
    fun saveUserSettings(settings: UserSettings) {
        val json = gson.toJson(settings)
        prefs.edit().putString("user_settings", json).apply()
    }

    fun getUserSettings(): UserSettings {
        val json = prefs.getString("user_settings", null) ?: return UserSettings()
        return gson.fromJson(json, UserSettings::class.java)
    }

    // Water Goal
    fun setWaterGoal(goal: Int) {
        val settings = getUserSettings()
        saveUserSettings(settings.copy(waterGoal = goal))
    }

    fun getWaterGoal(): Int {
        return getUserSettings().waterGoal
    }

    // Meditation Goal
    fun setMeditationGoal(goal: Int) {
        val settings = getUserSettings()
        saveUserSettings(settings.copy(meditationGoal = goal))
    }

    fun getMeditationGoal(): Int {
        return getUserSettings().meditationGoal
    }

    // First Launch
    fun isFirstLaunch(): Boolean {
        return prefs.getBoolean("first_launch", true)
    }

    fun setFirstLaunchComplete() {
        prefs.edit().putBoolean("first_launch", false).apply()
    }
}