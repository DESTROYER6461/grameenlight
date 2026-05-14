package com.example.grameenlight.ui.admin

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.grameenlight.data.model.ElectricityReport
import com.example.grameenlight.ui.admin.components.StatusChip

@Composable
fun AdminReportCard(
    report: ElectricityReport,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clickable {
                onClick()
            }
    ) {

        Column(
            modifier = Modifier.padding(16.dp)
        ) {

            Text(
                text = report.issueTitle,
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(8.dp))

            Text(text = "Issue Type: ${report.issueType}")
            Text(text = "Village: ${report.villageName}")
            Text(text = "Panchayat: ${report.panchayatName}")
            Text(text = "Pole No: ${report.poleNumber}")

            Spacer(modifier = Modifier.height(8.dp))

            StatusChip(status = report.status)
        }
    }
}