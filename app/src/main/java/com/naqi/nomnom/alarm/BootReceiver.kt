package com.naqi.nomnom.alarm

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent

class BootReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {

        if (intent.action == Intent.ACTION_BOOT_COMPLETED) {

            val scheduler = AlarmScheduler(context)

            scheduler.scheduleAlarm(8, 0, AlarmType.BREAKFAST, 1001)
            scheduler.scheduleAlarm(12, 0, AlarmType.LUNCH, 1002)
            scheduler.scheduleAlarm(19, 0, AlarmType.DINNER, 1003)
        }
    }
}