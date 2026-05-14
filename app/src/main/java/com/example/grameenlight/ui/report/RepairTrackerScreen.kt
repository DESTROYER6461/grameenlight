package com.example.grameenlight.ui.report

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController

data class TrackerItem(val id: String, val issue: String, val date: String, val status: String)

val sampleTrackerData = listOf(
    TrackerItem("GL-A1B2C3", "Wire Damage", "2024-06-01", "Fixed"),
    TrackerItem("GL-D4E5F6", "Bulb Short Circuit", "2024-06-03", "Assigned"),
    TrackerItem("GL-G7H8I9", "Daytime ON", "2024-06-05", "Pending")
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RepairTrackerScreen(navController: NavController) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Repair Tracker") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.padding(padding).padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            items(sampleTrackerData) { item ->
                TrackerCard(item)
            }
        }
    }
}

@Composable
fun TrackerCard(item: TrackerItem) {
    val statusColor = when (item.status) {
        "Fixed" -> Color(0xFF2E7D32)
        "Assigned" -> Color(0xFFFFA000)
        else -> Color(0xFFE53935)
    }
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(item.id, style = MaterialTheme.typography.titleMedium)
                Surface(
                    color = statusColor.copy(alpha = 0.15f),
                    shape = MaterialTheme.shapes.small
                ) {
                    Text(
                        item.status,
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        color = statusColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
            Spacer(Modifier.height(4.dp))
            Text(item.issue, style = MaterialTheme.typography.bodyMedium)
            Text(item.date, style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}
