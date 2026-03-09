package com.naqi.nomnom.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context

object NotificationHelper {

    const val CHANNEL_ID = "meal_reminder"

    fun createChannel(context: Context) {

        val channel = NotificationChannel(
            CHANNEL_ID,
            "Meal Reminder",
            NotificationManager.IMPORTANCE_HIGH
        )

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.createNotificationChannel(channel)
    }
}