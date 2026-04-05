package com.naqi.nomnom.ui.reminder

import android.app.Activity
import android.os.Build
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.naqi.nomnom.alarm.AlarmScheduler
import com.naqi.nomnom.alarm.AlarmType
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class ReminderActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val typeString = intent.getStringExtra("ALARM_TYPE")
        val alarmType = try {
            AlarmType.valueOf(typeString ?: "")
        } catch (_: Exception) {
            AlarmType.CUSTOM
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O_MR1) {
            setShowWhenLocked(true)
            setTurnScreenOn(true)
        } else {
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
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    // rememberSaveable → survive Activity recreation (Home button, config change)
    var isActionTaken by rememberSaveable { mutableStateOf(false) }
    var characterStateName by rememberSaveable { mutableStateOf(CharacterState.NORMAL.name) }
    val characterState = CharacterState.valueOf(characterStateName)

    // Safety net: user balik ke app setelah aksi → langsung finish
    LaunchedEffect(isActionTaken) {
        if (isActionTaken) {
            delay(1500)
            if (context is Activity) context.finish()
        }
    }

    val containerColor: Color = when (characterState) {
        CharacterState.NORMAL -> MaterialTheme.colorScheme.surfaceVariant
        CharacterState.HAPPY  -> Color(0xFFFFF9C4)
        CharacterState.SAD    -> Color(0xFFE3F2FD)
    }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { padding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(32.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Box(
                modifier = Modifier.size(200.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    shape = MaterialTheme.shapes.large,
                    color = containerColor,
                    tonalElevation = 4.dp
                ) {
                    Box(contentAlignment = Alignment.Center) {
                        Image(
                            painter = painterResource(id = characterState.drawableRes),
                            contentDescription = characterState.name,
                            modifier = Modifier.size(180.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = when (alarmType) {
                    AlarmType.BREAKFAST -> "Breakfast time 🍳"
                    AlarmType.LUNCH     -> "Lunch time 🍜"
                    AlarmType.DINNER    -> "Dinner time 🍛"
                    AlarmType.CUSTOM    -> "Time to eat!"
                },
                style = MaterialTheme.typography.headlineMedium
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = when (characterState) {
                    CharacterState.NORMAL -> ""
                    CharacterState.HAPPY  -> "Yay! Good job! 🎉"
                    CharacterState.SAD    -> "Okay... don't forget to eat 😢"
                },
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.secondary
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {

                Button(
                    enabled = !isActionTaken,
                    onClick = {
                        isActionTaken = true
                        characterStateName = CharacterState.HAPPY.name
                        scope.launch {
                            delay(900)
                            if (context is Activity) context.finish()
                        }
                    }
                ) {
                    Text("Ate")
                }

                OutlinedButton(
                    enabled = !isActionTaken,
                    onClick = {
                        isActionTaken = true
                        characterStateName = CharacterState.SAD.name
                        scope.launch {
                            snackbarHostState.showSnackbar(
                                message = "Okay, remind again in 10 minutes"
                            )
                            val scheduler = AlarmScheduler(context)
                            scheduler.scheduleAlarmInMinutes(
                                minutes = 10,
                                type = AlarmType.CUSTOM,
                                requestCode = 2001
                            )
                            if (context is Activity) context.finish()
                        }
                    }
                ) {
                    Text("Later")
                }
            }
        }
    }
}