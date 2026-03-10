package com.naqi.nomnom

import com.naqi.nomnom.alarm.AlarmScheduler
import com.naqi.nomnom.alarm.NotificationHelper
import com.naqi.nomnom.alarm.AlarmType
import android.Manifest
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

class MainActivity : ComponentActivity() {

    private fun scheduleDefaultAlarms() {

        val scheduler = AlarmScheduler(this)

        scheduler.scheduleAlarm(
            hour = 23,
            minute = 52,
            type = AlarmType.BREAKFAST,
            requestCode = 1001
        )

        scheduler.scheduleAlarm(
            hour = 12,
            minute = 0,
            type = AlarmType.LUNCH,
            requestCode = 1002
        )

        scheduler.scheduleAlarm(
            hour = 19,
            minute = 0,
            type = AlarmType.DINNER,
            requestCode = 1003
        )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {

            if (ContextCompat.checkSelfPermission(
                    this,
                    Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {

                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }

        NotificationHelper.createChannel(this)

        scheduleDefaultAlarms()

        setContent {
            NomNomApp()
        }
    }
}

@Composable
fun NomNomApp() {
    Text("NomNom is alive")
}