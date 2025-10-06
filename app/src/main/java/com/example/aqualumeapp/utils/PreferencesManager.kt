package com.example.aqualumeapp.utils

import android.content.Context
import android.content.SharedPreferences
import com.example.aqualumeapp.models.*
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class PreferencesManager(context: Context) {

    private val prefs: SharedPreferences =
        context.getSharedPreferences("AqualumePrefs", Context.MODE_PRIVATE)
    private val gson = Gson()

    // Water Logs
    fun saveWaterLog(log: WaterLog) {
        val logs = getWaterLogs().toMutableList()
        logs.add(log)
        saveWaterLogs(logs)
    }

    fun getWaterLogs(): List<WaterLog> {
        val json = prefs.getString("water_logs", null) ?: return emptyList()
        val type = object : TypeToken<List<WaterLog>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    private fun saveWaterLogs(logs: List<WaterLog>) {
        val json = gson.toJson(logs)
        prefs.edit().putString("water_logs", json).apply()
    }

    fun deleteWaterLog(logId: Long) {
        val logs = getWaterLogs().toMutableList()
        logs.removeAll { it.id == logId }
        saveWaterLogs(logs)
    }

    // Meditation Logs
    fun saveMeditationLog(log: MeditationLog) {
        val logs = getMeditationLogs().toMutableList()
        logs.add(log)
        saveMeditationLogs(logs)
    }

    fun getMeditationLogs(): List<MeditationLog> {
        val json = prefs.getString("meditation_logs", null) ?: return emptyList()
        val type = object : TypeToken<List<MeditationLog>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    private fun saveMeditationLogs(logs: List<MeditationLog>) {
        val json = gson.toJson(logs)
        prefs.edit().putString("meditation_logs", json).apply()
    }

    fun deleteMeditationLog(logId: Long) {
        val logs = getMeditationLogs().toMutableList()
        logs.removeAll { it.id == logId }
        saveMeditationLogs(logs)
    }

    fun updateMeditationLog(logId: Long, duration: Int, completed: Boolean) {
        val logs = getMeditationLogs().toMutableList()
        val index = logs.indexOfFirst { it.id == logId }
        if (index != -1) {
            logs[index] = logs[index].copy(duration = duration, isCompleted = completed)
            saveMeditationLogs(logs)
        }
    }

    fun getMeditationGoal(): Int {
        return prefs.getInt("meditation_goal", 20)
    }

    // Mood Logs
    fun saveMoodLog(log: MoodLog) {
        val logs = getMoodLogs().toMutableList()
        logs.add(log)
        saveMoodLogs(logs)
    }

    fun getMoodLogs(): List<MoodLog> {
        val json = prefs.getString("mood_logs", null) ?: return emptyList()
        val type = object : TypeToken<List<MoodLog>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    private fun saveMoodLogs(logs: List<MoodLog>) {
        val json = gson.toJson(logs)
        prefs.edit().putString("mood_logs", json).apply()
    }

    fun deleteMoodLog(logId: Long) {
        val logs = getMoodLogs().toMutableList()
        logs.removeAll { it.id == logId }
        saveMoodLogs(logs)
    }

    fun updateMoodLog(logId: Long, moodName: String, moodEmoji: String) {
        val logs = getMoodLogs().toMutableList()
        val index = logs.indexOfFirst { it.id == logId }
        if (index != -1) {
            logs[index] = logs[index].copy(mood = moodName, emoji = moodEmoji)
            saveMoodLogs(logs)
        }
    }

    // Tasks
    fun saveTask(task: Task) {
        val tasks = getAllTasks().toMutableList()
        tasks.add(task)
        saveAllTasks(tasks)
    }

    fun getAllTasks(): List<Task> {
        val json = prefs.getString("tasks", null) ?: return emptyList()
        val type = object : TypeToken<List<Task>>() {}.type
        return gson.fromJson(json, type) ?: emptyList()
    }

    private fun saveAllTasks(tasks: List<Task>) {
        val json = gson.toJson(tasks)
        prefs.edit().putString("tasks", json).apply()
    }

    fun updateTaskCompletion(taskId: Long, completed: Boolean) {
        val tasks = getAllTasks().toMutableList()
        val index = tasks.indexOfFirst { it.id == taskId }
        if (index != -1) {
            tasks[index] = tasks[index].copy(isCompleted = completed)
            saveAllTasks(tasks)
        }
    }

    fun deleteTask(taskId: Long) {
        val tasks = getAllTasks().toMutableList()
        tasks.removeAll { it.id == taskId }
        saveAllTasks(tasks)
    }

    // User Settings
    fun getUserSettings(): UserSettings {
        val json = prefs.getString("user_settings", null)
        return if (json != null) {
            gson.fromJson(json, UserSettings::class.java)
        } else {
            UserSettings()
        }
    }

    fun saveUserSettings(settings: UserSettings) {
        val json = gson.toJson(settings)
        prefs.edit().putString("user_settings", json).apply()
    }

    // First Launch
    fun isFirstLaunch(): Boolean {
        return prefs.getBoolean("is_first_launch", true)
    }

    fun setFirstLaunchComplete() {
        prefs.edit().putBoolean("is_first_launch", false).apply()
    }
}