package com.naqi.nomnom.ui.reminder

import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.naqi.nomnom.alarm.AlarmScheduler
import com.naqi.nomnom.alarm.AlarmType
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import android.app.Activity
import androidx.compose.ui.platform.LocalContext
import androidx.compose.foundation.Image
import androidx.compose.ui.res.painterResource
import com.naqi.nomnom.R


class ReminderActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val typeString = intent.getStringExtra("ALARM_TYPE")
        val alarmType = try {
            AlarmType.valueOf(typeString ?: "")
        } catch (_: Exception) {
            AlarmType.CUSTOM
        }

        // untuk Android versi baru
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
            // fallback Android lama
            window.addFlags(
                WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON or
                        WindowManager.LayoutParams.FLAG_ALLOW_LOCK_WHILE_SCREEN_ON
            )
        }

        setContent {
            ReminderScreen(alarmType)
        }
    }
}

@Composable
fun ReminderScreen(alarmType: AlarmType) {

    val context = LocalContext.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        // Placeholder untuk karakter NomNom
        Box(
            modifier = Modifier
                .size(200.dp),
            contentAlignment = Alignment.Center
        ) {

            Surface(
                modifier = Modifier.fillMaxSize(),
                shape = MaterialTheme.shapes.large,
                tonalElevation = 4.dp
            ) {

                Box(
                    contentAlignment = Alignment.Center
                ) {

                    Image(
                        painter = painterResource(id = R.drawable.nomnom_character),
                        contentDescription = "NomNom Character",
                        modifier = Modifier.size(180.dp)
                    )

                }
            }

        }

        Spacer(modifier = Modifier.height(24.dp))

        val message = when (alarmType) {

            AlarmType.BREAKFAST -> "Breakfast time 🍳"

            AlarmType.LUNCH -> "Lunch time 🍜"

            AlarmType.DINNER -> "Dinner time 🍛"

            AlarmType.CUSTOM -> "Time to eat!"
        }

        Text(
            text = message,
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(32.dp))

        Row(
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {

            Button(
                onClick = {

                    if (context is Activity) {
                        context.finish()
                    }

                }
            ) {
                Text("Ate")
            }

            OutlinedButton(
                onClick = {

                    val scheduler = AlarmScheduler(context)

                    scheduler.scheduleAlarmInMinutes(
                        minutes = 10,
                        type = AlarmType.CUSTOM,
                        requestCode = 2001
                    )

                    if (context is Activity) {
                        context.finish()
                    }

                }
            ) {
                Text("Later")
            }

        }
    }
}