package com.example.myapplication

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.myapplication.ui.theme.MyApplicationTheme

data class Stop(val name: String, val distance: Double)

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val stops = listOf(
//            Stop("Stop 1", 1.0), Stop("Stop 2", 1.5), Stop("Stop 3", 2.0),
//            Stop("Stop 4", 2.5), Stop("Stop 5", 3.0), Stop("Stop 6", 3.5),
//            Stop("Stop 7", 4.0), Stop("Stop 8", 4.5), Stop("Stop 9", 5.0),
//            Stop("Stop 10", 5.5), Stop("Stop 11", 1.5),Stop("Stop 12", 3.0),
//            Stop("Stop 13", 13.0)
//        )
        val stops = listOf(
            Stop("Stop 1", 1.0), Stop("Stop 2", 1.5), Stop("Stop 3", 2.0),
            Stop("Stop 4", 2.5), Stop("Stop 5", 3.0), Stop("Stop 6", 3.5),
            Stop("Stop 7", 4.0), Stop("Stop 8", 4.5), Stop("Stop 9", 5.0),
            Stop("Stop 10", 5.5),
        )


        setContent {
            MyApplicationTheme {
                JourneyScreen(stops)
            }
        }
    }
}

@Composable
fun JourneyScreen(stops: List<Stop>) {
    var currentStopIndex by remember { mutableStateOf(-1) }
    var useMiles by remember { mutableStateOf(false) }
    val distanceCoveredInKm = stops.take(currentStopIndex + 1).sumOf { it.distance }
    val totalDistanceInKm = stops.sumOf { it.distance }
    val progress = if (totalDistanceInKm > 0) distanceCoveredInKm / totalDistanceInKm else 0f
    val progressPercentage = ((progress).toInt() * 100)

    Column(modifier = Modifier.padding(16.dp)) {

        Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.fillMaxWidth()) {
            Button(onClick = { useMiles = !useMiles }, modifier = Modifier.weight(1f)) {
                Text(text = if (useMiles) "Switch to Kilometers" else "Switch to Miles")
            }
            Spacer(modifier = Modifier.width(8.dp))
            Button(
                onClick = { currentStopIndex = -1 },
                modifier = Modifier.weight(1f)
            ) {
                Text("Reset Journey")
            }
        }

        LinearProgressIndicator(progress = progress.toFloat(), modifier = Modifier.padding(vertical = 8.dp).fillMaxWidth())


        Text(
            text = "Covered: ${
                if (useMiles) "%.2f miles".format(distanceCoveredInKm * 0.621371) else "$distanceCoveredInKm km"
            }, Left: ${
                if (useMiles) "%.2f miles".format((totalDistanceInKm - distanceCoveredInKm) * 0.621371) else "${totalDistanceInKm - distanceCoveredInKm} km"
            }",
            style = MaterialTheme.typography.bodyLarge
        )

        Button(onClick = { if (currentStopIndex < stops.size - 1) currentStopIndex++ }) {
            Text("Next Stop")
        }

        if (stops.size <= 10) {
            Column(modifier = Modifier.weight(1f)) {
                stops.forEachIndexed { index, stop ->
                    StopItem(stop, useMiles, index <= currentStopIndex)
                }
            }
        } else {
            LazyColumn(modifier = Modifier.weight(1f)) {
                itemsIndexed(stops) { index, stop ->
                    StopItem(stop, useMiles, index <= currentStopIndex)
                }
            }
        }
    }
}

@Composable
fun StopItem(stop: Stop, useMiles: Boolean, isVisited: Boolean) {
    Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(vertical = 8.dp)) {
        Text(
            text = "•",
            color = if (isVisited) Color.Green else Color.LightGray,
            fontSize = 32.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(end = 8.dp)
        )
        Column {
            Text(text = stop.name, style = MaterialTheme.typography.bodyLarge)
            Text(
                text = if (useMiles) "%.2f miles".format(stop.distance * 0.621371) else "${stop.distance} km",
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
