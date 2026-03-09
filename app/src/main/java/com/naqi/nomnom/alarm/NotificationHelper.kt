package com.naqi.nomnom.alarm

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import androidx.core.app.NotificationCompat

object NotificationHelper {

    const val CHANNEL_ID = "nomnom_channel"

    fun createChannel(context: Context) {

        val channel = NotificationChannel(
            CHANNEL_ID,
            "NomNom Reminder",
            NotificationManager.IMPORTANCE_HIGH
        )

        val manager =
            context.getSystemService(NotificationManager::class.java)

        manager.createNotificationChannel(channel)
    }

    fun showNotification(context: Context, type: AlarmType) {

        val message = when (type) {

            AlarmType.BREAKFAST -> "Breakfast time 🍳"

            AlarmType.LUNCH -> "Lunch time 🍜"

            AlarmType.DINNER -> "Dinner time 🍛"

            AlarmType.CUSTOM -> "Time to eat!"
        }

        val builder = NotificationCompat.Builder(context, CHANNEL_ID)
            .setSmallIcon(android.R.drawable.ic_dialog_info)
            .setContentTitle("NomNom")
            .setContentText(message)
            .setPriority(NotificationCompat.PRIORITY_HIGH)

        val manager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        manager.notify(type.ordinal, builder.build())
    }
}