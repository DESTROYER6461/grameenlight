package com.example.grameenlight.ui.admin

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.grameenlight.navigation.Screen
import com.example.grameenlight.ui.admin.components.StatCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    navController: NavController,
    viewModel: AdminViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    var selectedTab by remember { mutableStateOf(0) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("Admin Control Center", style = MaterialTheme.typography.titleLarge, fontWeight = FontWeight.Bold)
                        Text("Panchayat Management", style = MaterialTheme.typography.bodySmall)
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                ),
                actions = {
                    IconButton(onClick = {
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0)
                        }
                    }) {
                        Icon(Icons.AutoMirrored.Filled.ExitToApp, contentDescription = "Logout", tint = Color.White)
                    }
                }
            )
        },
        bottomBar = { AdminBottomBar(navController) }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.surface)
        ) {
            // Admin Tabs
            TabRow(selectedTabIndex = selectedTab) {
                Tab(selected = selectedTab == 0, onClick = { selectedTab = 0 }) {
                    Text("Overview", modifier = Modifier.padding(16.dp))
                }
                Tab(selected = selectedTab == 1, onClick = { selectedTab = 1 }) {
                    Text("Complaints", modifier = Modifier.padding(16.dp))
                }
                Tab(selected = selectedTab == 2, onClick = { selectedTab = 2 }) {
                    Text("Poles", modifier = Modifier.padding(16.dp))
                }
            }

            when (selectedTab) {
                0 -> AdminOverviewContent(uiState, navController)
                1 -> AdminComplaintsContent(uiState, navController)
                2 -> AdminPolesContent()
            }
        }
    }
}

@Composable
fun AdminOverviewContent(uiState: AdminUiState, navController: NavController) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {
        item {
            Text("Village Infrastructure Stats", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                StatCard("Total", uiState.reports.size.toString(), Modifier.weight(1f))
                StatCard("Alerts", uiState.reports.count { it.isEmergency }.toString(), Modifier.weight(1f))
            }
        }

        item {
            Text("Management Tools", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
            Spacer(Modifier.height(12.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                AdminActionItem(Icons.Filled.Person, "Users", Color(0xFF673AB7)) {}
                AdminActionItem(Icons.Filled.Settings, "Config", Color(0xFF607D8B)) { }
                AdminActionItem(Icons.Filled.Info, "Stats", Color(0xFF009688)) {}
                AdminActionItem(Icons.Filled.Notifications, "Alert", Color(0xFFE91E63)) {}
            }
        }

        item {
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.secondaryContainer)
            ) {
                Column(Modifier.padding(20.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(Icons.Filled.Notifications, contentDescription = null, tint = MaterialTheme.colorScheme.onSecondaryContainer)
                        Spacer(Modifier.width(12.dp))
                        Text("System Notifications", style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text("3 New emergency reports need immediate attention.", style = MaterialTheme.typography.bodyMedium)
                }
            }
        }
    }
}

@Composable
fun AdminComplaintsContent(uiState: AdminUiState, navController: NavController) {
    var searchText by remember { mutableStateOf("") }
    val filteredReports = uiState.reports.filter {
        it.issueTitle.contains(searchText, true) || it.villageName.contains(searchText, true)
    }

    Column(modifier = Modifier.padding(16.dp)) {
        OutlinedTextField(
            value = searchText,
            onValueChange = { searchText = it },
            label = { Text("Search Complaints") },
            modifier = Modifier.fillMaxWidth(),
            leadingIcon = { Icon(Icons.Filled.Search, contentDescription = null) },
            shape = RoundedCornerShape(12.dp)
        )
        Spacer(Modifier.height(16.dp))
        
        if (uiState.loading) {
            Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) { CircularProgressIndicator() }
        } else {
            LazyColumn(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                items(filteredReports) { report ->
                    AdminReportCard(
                        report = report,
                        onClick = { navController.navigate("admin_report_details/${report.reportId}") }
                    )
                }
            }
        }
    }
}

@Composable
fun AdminPolesContent() {
    Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Icon(Icons.Filled.Build, contentDescription = null, modifier = Modifier.size(64.dp), tint = Color.Gray)
            Text("Infrastructure Management", style = MaterialTheme.typography.titleLarge)
            Text("Remote pole controlling coming soon", color = Color.Gray)
        }
    }
}

@Composable
fun AdminActionItem(icon: ImageVector, label: String, color: Color, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier.clickable { onClick() }
    ) {
        Box(
            modifier = Modifier
                .size(56.dp)
                .clip(CircleShape)
                .background(color.copy(alpha = 0.1f)),
            contentAlignment = Alignment.Center
        ) {
            Icon(icon, contentDescription = label, tint = color, modifier = Modifier.size(24.dp))
        }
        Spacer(Modifier.height(8.dp))
        Text(label, style = MaterialTheme.typography.bodySmall, fontWeight = FontWeight.Medium)
    }
}

@Composable
fun AdminBottomBar(navController: NavController) {
    val items = listOf(
        Triple("Admin", Icons.Filled.Settings, Screen.Admin.route),
        Triple("Map", Icons.Filled.LocationOn, Screen.Map.route),
        Triple("Profile", Icons.Filled.Person, Screen.Profile.route)
    )

    NavigationBar(
        containerColor = MaterialTheme.colorScheme.surface,
        tonalElevation = 8.dp
    ) {
        val currentRoute = navController.currentBackStackEntry?.destination?.route

        items.forEach { (label, icon, route) ->
            NavigationBarItem(
                selected = currentRoute == route,
                onClick = {
                    if (currentRoute != route) {
                        navController.navigate(route) {
                            popUpTo(Screen.Admin.route) { saveState = true }
                            launchSingleTop = true
                            restoreState = true
                        }
                    }
                },
                icon = { Icon(icon, contentDescription = label) },
                label = { Text(label) }
            )
        }
    }
}
