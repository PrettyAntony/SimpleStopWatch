package com.example.stopwatchapplication

import android.os.Bundle
import android.os.SystemClock
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import java.util.concurrent.TimeUnit


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            StopwatchApp()
        }
    }
}

@Composable
fun StopwatchApp() {
    // Define a basic theme with a light color scheme
    val colorScheme = lightColorScheme(
        primary = Color(0xFF6200EE), // Purple
        onPrimary = Color(0xFFFFFFFF),
        secondary = Color(0xFF03DAC6), // Teal
        onSecondary = Color(0xFF000000),
        background = Color(0xFFFFFFFF),
        onBackground = Color(0xFF000000),
        surface = Color(0xFFFFFFFF),
        onSurface = Color(0xFF000000),
        error = Color(0xFFB00020), // Red
        onError = Color(0xFFFFFFFF)
    )

    // Use the MaterialTheme with the defined color scheme
    MaterialTheme(
        colorScheme = colorScheme,
        content = { StopwatchScreen() }
    )
}

@Composable
fun StopwatchScreen() {
    var isRunning by remember { mutableStateOf(false) }
    var elapsedTime by remember { mutableLongStateOf(0L) }
    var startTime by remember { mutableLongStateOf(0L) }

    // Update elapsed time when running
    LaunchedEffect(isRunning) {
        if (isRunning) {
            startTime = SystemClock.elapsedRealtime() - elapsedTime
            while (isRunning) {
                elapsedTime = SystemClock.elapsedRealtime() - startTime
                delay(50) // Update time approximately every 50ms
            }
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = formatElapsedTime(elapsedTime),
            fontSize = 48.sp,
            color = Color(0xFF000000),
            modifier = Modifier
                .background(Color(0xFF808080))
                .padding(16.dp)
                .fillMaxWidth()
                .wrapContentWidth(align = Alignment.CenterHorizontally)
        )
        Spacer(modifier = Modifier.height(32.dp))
        Row {
            Button(
                onClick = {
                    isRunning = true
                    startTime = SystemClock.elapsedRealtime() - elapsedTime // Update startTime for resuming
                },
                modifier = Modifier.padding(8.dp)
            ){
                Text("Start")
            }
            Button(
                onClick = { isRunning = false },
                modifier = Modifier.padding(8.dp)
            ){
                Text("Stop")
            }
            Button(
                onClick = {
                    isRunning = false
                    elapsedTime = 0L
                    startTime = SystemClock.elapsedRealtime()
                },
                modifier = Modifier.padding(8.dp)
            ){
                Text("Reset")
            }
        }
    }
}

@Composable
fun formatElapsedTime(elapsedTime: Long): String {
    val minutes = TimeUnit.MILLISECONDS.toMinutes(elapsedTime)
    val seconds = TimeUnit.MILLISECONDS.toSeconds(elapsedTime) % 60
    val milliseconds = (elapsedTime % 1000) / 10
    return String.format("%02d:%02d:%02d", minutes, seconds, milliseconds)
}