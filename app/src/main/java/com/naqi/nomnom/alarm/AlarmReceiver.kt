package com.naqi.nomnom.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

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
                scheduler.scheduleAlarm(8, 0, AlarmType.BREAKFAST, 1001)
            }

            AlarmType.LUNCH -> {
                scheduler.scheduleAlarm(12, 0, AlarmType.LUNCH, 1002)
            }

            AlarmType.DINNER -> {
                scheduler.scheduleAlarm(19, 0, AlarmType.DINNER, 1003)
            }

            AlarmType.CUSTOM -> {
                // nanti custom alarm akan kita handle
            }
        }
    }
}