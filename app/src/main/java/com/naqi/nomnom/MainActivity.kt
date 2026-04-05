package com.naqi.nomnom

import android.Manifest
import android.app.AlarmManager
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.naqi.nomnom.alarm.AlarmScheduler
import com.naqi.nomnom.alarm.AlarmType
import com.naqi.nomnom.alarm.NotificationHelper

class MainActivity : ComponentActivity() {

    private fun scheduleDefaultAlarms() {
        val scheduler = AlarmScheduler(this)
        scheduler.scheduleAlarm(hour = 3,  minute = 48, type = AlarmType.BREAKFAST, requestCode = 1001)
        scheduler.scheduleAlarm(hour = 12, minute = 0,  type = AlarmType.LUNCH,     requestCode = 1002)
        scheduler.scheduleAlarm(hour = 19, minute = 0,  type = AlarmType.DINNER,    requestCode = 1003)
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Step 1: Request POST_NOTIFICATIONS (Android 13+)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (ContextCompat.checkSelfPermission(
                    this, Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.POST_NOTIFICATIONS),
                    1
                )
            }
        }

        // Step 2: Request SCHEDULE_EXACT_ALARM (Android 12+)
        // Ini yang selama ini hilang — tanpa ini canScheduleExactAlarms() = false
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            if (!alarmManager.canScheduleExactAlarms()) {
                val intent = Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM).apply {
                    data = Uri.parse("package:$packageName")
                }
                startActivity(intent)
                // Jangan scheduleDefaultAlarms() dulu — permission belum granted
                // onResume() akan handle ini
            }
        }

        NotificationHelper.createChannel(this)
        scheduleDefaultAlarms()

        setContent {
            NomNomApp()
        }
    }

    // Ketika user kembali dari Settings setelah grant permission
    override fun onResume() {
        super.onResume()

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            val alarmManager = getSystemService(ALARM_SERVICE) as AlarmManager
            if (alarmManager.canScheduleExactAlarms()) {
                scheduleDefaultAlarms()
            }
        }
    }
}

@Composable
fun NomNomApp() {
    Text("NomNom is alive")
}