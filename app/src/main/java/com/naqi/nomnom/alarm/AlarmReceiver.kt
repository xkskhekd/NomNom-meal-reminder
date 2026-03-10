package com.naqi.nomnom.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        Log.d("NomNomAlarm", "AlarmReceiver triggered")

        val typeString = intent.getStringExtra("ALARM_TYPE")

        val alarmType = try {
            AlarmType.valueOf(typeString ?: "")
        } catch (_: Exception) {
            AlarmType.CUSTOM
        }

        NotificationHelper.showNotification(context, alarmType)

        rescheduleAlarm(context, alarmType)
    }

    private fun rescheduleAlarm(context: Context, type: AlarmType) {

        val scheduler = AlarmScheduler(context)

        when (type) {

            AlarmType.BREAKFAST -> {
                scheduler.scheduleAlarm(11, 26, AlarmType.BREAKFAST, 1001)
            }

            AlarmType.LUNCH -> {
                scheduler.scheduleAlarm(12, 0, AlarmType.LUNCH, 1002)
            }

            AlarmType.DINNER -> {
                scheduler.scheduleAlarm(19, 0, AlarmType.DINNER, 1003)
            }

            AlarmType.CUSTOM -> {
            }
        }
    }
}