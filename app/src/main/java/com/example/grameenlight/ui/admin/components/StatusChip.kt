package com.example.grameenlight.ui.admin.components

import androidx.compose.material3.AssistChip
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable

@Composable
fun StatusChip(status: String) {

    AssistChip(
        onClick = {},
        label = {
            Text(status)
        }
    )
}