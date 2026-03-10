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

class ReminderActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

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
            ReminderScreen()
        }
    }
}

@Composable
fun ReminderScreen() {

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
                .size(180.dp)
        ) {
            Text("NomNom")
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "Time to eat!",
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