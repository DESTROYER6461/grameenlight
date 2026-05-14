package com.example.grameenlight.ui.tracker

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import kotlinx.coroutines.delay

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

// Keeping your Splash Screen code but renaming it to avoid conflict with the main SplashScreen
@Composable
fun RepairTrackerSplashScreen(onReady: () -> Unit) {
    val scale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(dampingRatio = Spring.DampingRatioMediumBouncy),
        label = "scale"
    )
    val glowAlpha by rememberInfiniteTransition(label = "glow").animateFloat(
        initialValue = 0.4f, targetValue = 1f,
        animationSpec = infiniteRepeatable(tween(800), RepeatMode.Reverse),
        label = "alpha"
    )

    LaunchedEffect(Unit) {
        delay(2200)
        onReady()
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E1A)),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Text("💡", fontSize = 72.sp, modifier = Modifier.scale(scale))
            Spacer(Modifier.height(16.dp))
            Text(
                "Grameen-Light",
                style = MaterialTheme.typography.headlineLarge,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFFFC107).copy(alpha = glowAlpha)
            )
            Text(
                "Smart Village Energy",
                style = MaterialTheme.typography.bodyLarge,
                color = Color.White.copy(alpha = 0.7f)
            )
        }
    }
}
