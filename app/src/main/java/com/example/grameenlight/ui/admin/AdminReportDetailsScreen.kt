package com.example.grameenlight.ui.admin

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grameenlight.data.model.ElectricityReport
import com.example.grameenlight.ui.components.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReportDetailsScreen(
    report: ElectricityReport,
    navController: NavController,
    viewModel: AdminViewModel
) {
    var expanded by remember { mutableStateOf(false) }
    var selectedStatus by remember { mutableStateOf(report.status) }

    val statusOptions = listOf(
        "Pending",
        "Assigned",
        "In Progress",
        "Resolved",
        "Rejected"
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Report Details") },
                navigationIcon = { BackButton(navController) },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
                .padding(16.dp)
                .verticalScroll(rememberScrollState())
        ) {
            Card(
                modifier = Modifier.fillMaxWidth(),
                elevation = CardDefaults.cardElevation(2.dp)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = report.issueTitle,
                        style = MaterialTheme.typography.headlineSmall
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = "Status: ${report.status}", color = MaterialTheme.colorScheme.primary)
                    Text(text = "Priority: ${report.priority}")
                    if (report.isEmergency) {
                        Text(text = "EMERGENCY", color = MaterialTheme.colorScheme.error, style = MaterialTheme.typography.labelLarge)
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Description", style = MaterialTheme.typography.titleMedium)
            Text(text = report.description)

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Location Information", style = MaterialTheme.typography.titleMedium)
            DetailRow(label = "District", value = report.district)
            DetailRow(label = "Taluk", value = report.taluk)
            DetailRow(label = "Panchayat", value = report.panchayatName)
            DetailRow(label = "Village", value = report.villageName)
            DetailRow(label = "Ward", value = report.wardName)
            DetailRow(label = "Pole No", value = report.poleNumber)
            DetailRow(label = "Landmark", value = report.landmark)
            DetailRow(label = "Address", value = report.address)

            Spacer(modifier = Modifier.height(24.dp))

            Divider()

            Spacer(modifier = Modifier.height(16.dp))

            Text(text = "Update Status", style = MaterialTheme.typography.titleMedium)
            Spacer(modifier = Modifier.height(8.dp))

            ExposedDropdownMenuBox(
                expanded = expanded,
                onExpandedChange = { expanded = !expanded }
            ) {
                OutlinedTextField(
                    value = selectedStatus,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text("Current Status") },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
                    modifier = Modifier.menuAnchor().fillMaxWidth()
                )

                ExposedDropdownMenu(
                    expanded = expanded,
                    onDismissRequest = { expanded = false }
                ) {
                    statusOptions.forEach { status ->
                        DropdownMenuItem(
                            text = { Text(status) },
                            onClick = {
                                selectedStatus = status
                                expanded = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    viewModel.updateStatus(report.reportId, selectedStatus)
                    navController.popBackStack()
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Update Report Status")
            }
        }
    }
}

@Composable
fun DetailRow(label: String, value: String) {
    if (value.isNotEmpty()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 4.dp)
        ) {
            Text(text = "$label: ", style = MaterialTheme.typography.bodyMedium, color = MaterialTheme.colorScheme.secondary)
            Text(text = value, style = MaterialTheme.typography.bodyMedium)
        }
    }
}
