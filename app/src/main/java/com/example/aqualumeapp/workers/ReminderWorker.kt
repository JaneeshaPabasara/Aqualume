package com.example.aqualumeapp.workers
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.work.Worker
import androidx.work.WorkerParameters
import com.example.aqualumeapp.MainActivity
import com.example.aqualumeapp.R

class ReminderWorker(
    context: Context,
    params: WorkerParameters
) : Worker(context, params) {

    override fun doWork(): Result {
        val type = inputData.getString("type") ?: return Result.failure()

        when (type) {
            "water" -> sendWaterReminder()
            "meditation" -> sendMeditationReminder()
            "journal" -> sendJournalReminder()
        }

        return Result.success()
    }

    private fun sendWaterReminder() {
        sendNotification(
            "Hydration Reminder",
            "Time to drink water! Stay hydrated 💧",
            1
        )
    }

    private fun sendMeditationReminder() {
        sendNotification(
            "Meditation Reminder",
            "Take a moment to meditate and relax 🧘",
            2
        )
    }

    private fun sendJournalReminder() {
        sendNotification(
            "Journal Reminder",
            "How are you feeling today? Write in your journal 📝",
            3
        )
    }

    private fun sendNotification(title: String, message: String, notificationId: Int) {
        val notificationManager = applicationContext.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "wellness_reminders",
                "Wellness Reminders",
                NotificationManager.IMPORTANCE_DEFAULT
            ).apply {
                description = "Reminders for water, meditation, and journaling"
            }
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(applicationContext, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            applicationContext,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val notification = NotificationCompat.Builder(applicationContext, "wellness_reminders")
            .setSmallIcon(R.drawable.ic_notification)
            .setContentTitle(title)
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(notificationId, notification)
    }
}