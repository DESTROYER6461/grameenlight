package com.example.grameenlight.ui.admin

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grameenlight.ui.components.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminScreen(navController: NavController) {

    val reports = remember {
        mutableStateListOf(
            Triple("GL-A1B2C3", "Wire Damage — Bengaluru",   "Pending"),
            Triple("GL-D4E5F6", "Bulb Short — Mysuru",       "Assigned"),
            Triple("GL-G7H8I9", "Daytime ON — Hubli",        "Pending"),
            Triple("GL-J1K2L3", "Power Outage — Dharwad",    "Pending"),
            Triple("GL-M4N5O6", "Wire Damage — Belagavi",    "Fixed")
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text("Admin Panel") },
                navigationIcon = { BackButton(navController) },
                colors         = TopAppBarDefaults.topAppBarColors(
                    containerColor    = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        LazyColumn(
            modifier            = Modifier
                .padding(padding)
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                Text(
                    text  = "All Complaints",
                    style = MaterialTheme.typography.titleMedium
                )
            }

            items(reports.size) { index ->
                val (id, desc, status) = reports[index]
                AdminReportCard(
                    id          = id,
                    description = desc,
                    status      = status,
                    onStatusChange = { newStatus ->
                        reports[index] = Triple(id, desc, newStatus)
                    }
                )
            }
        }
    }
}

@Composable
fun AdminReportCard(
    id:             String,
    description:    String,
    status:         String,
    onStatusChange: (String) -> Unit
) {
    val statusColor = when (status) {
        "Fixed"    -> Color(0xFF2E7D32)
        "Assigned" -> Color(0xFFFFA000)
        else       -> Color(0xFFE53935)
    }

    Card(modifier = Modifier.fillMaxWidth()) {
        Column(Modifier.padding(16.dp)) {
            Text(id,
                style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(2.dp))
            Text(description,
                style = MaterialTheme.typography.bodyMedium)
            Spacer(Modifier.height(6.dp))
            Text(
                text  = "Status: $status",
                color = statusColor,
                style = MaterialTheme.typography.bodySmall
            )
            Spacer(Modifier.height(10.dp))
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                if (status == "Pending") {
                    OutlinedButton(
                        onClick = { onStatusChange("Assigned") }
                    ) { Text("Assign") }
                }
                if (status == "Assigned") {
                    Button(
                        onClick = { onStatusChange("Fixed") }
                    ) { Text("Mark Fixed") }
                }
                if (status == "Fixed") {
                    Text(
                        text  = "✅ Resolved",
                        color = statusColor,
                        style = MaterialTheme.typography.bodySmall
                    )
                }
            }
        }
    }
}