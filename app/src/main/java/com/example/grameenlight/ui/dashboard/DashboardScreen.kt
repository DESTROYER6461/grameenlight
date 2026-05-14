package com.example.grameenlight.ui.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.grameenlight.navigation.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DashboardScreen(
    navController: NavController,
    vm: DashboardViewModel = viewModel()
) {
    val stats by vm.stats.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Grameen-Light") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Profile.route)
                    }) {
                        Icon(Icons.Filled.Person, contentDescription = "Profile",
                            tint = MaterialTheme.colorScheme.onPrimary)
                    }
                }
            )
        },
        bottomBar = { GrameenBottomBar(navController) }
    ) { padding ->
        LazyColumn(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text("Realtime Overview", style = MaterialTheme.typography.titleMedium)
            }
            
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LiveStatCard(
                        title = "Pending",
                        count = stats.pending,
                        color = Color.Red,
                        modifier = Modifier.weight(1f)
                    )
                    LiveStatCard(
                        title = "Assigned",
                        count = stats.assigned,
                        color = Color(0xFF2962FF),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    LiveStatCard(
                        title = "Working",
                        count = stats.working,
                        color = Color(0xFFFF9800),
                        modifier = Modifier.weight(1f)
                    )
                    LiveStatCard(
                        title = "Resolved",
                        count = stats.resolved,
                        color = Color(0xFF2E7D32),
                        modifier = Modifier.weight(1f)
                    )
                }
            }

            item {
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text("Total Reports", style = MaterialTheme.typography.bodyMedium)
                            Text("${stats.total}", style = MaterialTheme.typography.headlineMedium, fontWeight = FontWeight.Bold)
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text("Energy Saved", style = MaterialTheme.typography.bodyMedium)
                            Text("${stats.energySaved} kWh", style = MaterialTheme.typography.headlineSmall, color = Color(0xFF2E7D32), fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            item { EnergyTipCard() }
            
            item {
                Button(
                    onClick = { navController.navigate(Screen.Report.route) },
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Text("⚡  Report a Streetlight Issue",
                        style = MaterialTheme.typography.titleMedium)
                }
            }
            
            item {
                OutlinedButton(
                    onClick = { navController.navigate(Screen.Tracker.route) },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("View Repair Tracker")
                }
            }
        }
    }
}

@Composable
fun LiveStatCard(
    title: String,
    count: Int,
    color: Color,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier
            .height(120.dp),
        colors = CardDefaults.cardColors(
            containerColor = color.copy(alpha = 0.12f)
        ),
        shape = RoundedCornerShape(20.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = count.toString(),
                fontSize = 34.sp,
                fontWeight = FontWeight.Bold,
                color = color
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                color = Color.DarkGray
            )
        }
    }
}

@Composable
fun EnergyTipCard() {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.secondaryContainer)
    ) {
        Column(Modifier.padding(16.dp)) {
            Text("💡 Energy Tip", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            Text(
                "Reporting lights ON in daytime saves up to 8 hours of energy per pole daily.",
                style = MaterialTheme.typography.bodyMedium
            )
        }
    }
}

@Composable
fun GrameenBottomBar(navController: NavController) {
    val items = listOf(
        Triple("Home",     Icons.Filled.Home,        Screen.Dashboard.route),
        Triple("Report",   Icons.Filled.Warning,     Screen.Report.route),
        Triple("Feedback", Icons.Filled.Star,        Screen.Feedback.route),
        Triple("Map",      Icons.Filled.LocationOn,  Screen.Map.route)
    )

    NavigationBar {
        val currentRoute = navController
            .currentBackStackEntry?.destination?.route

        items.forEach { (label, icon, route) ->
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = {
                    navController.navigate(route) {
                        popUpTo(Screen.Dashboard.route) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) }
            )
        }
    }
}
