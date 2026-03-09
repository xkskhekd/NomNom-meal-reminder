package com.naqi.nomnom

import com.naqi.nomnom.alarm.AlarmScheduler
import com.naqi.nomnom.alarm.NotificationHelper
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

        val scheduler = AlarmScheduler(this)

        scheduler.scheduleAlarm(23, 32)

        setContent {
            NomNomApp()
        }
    }
}

@Composable
fun NomNomApp() {
    Text("NomNom is alive")
}