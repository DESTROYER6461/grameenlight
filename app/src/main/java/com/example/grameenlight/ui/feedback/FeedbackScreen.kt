package com.example.grameenlight.ui.feedback

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.grameenlight.ui.components.BackButton

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FeedbackScreen(navController: NavController) {
    var employeeName by remember { mutableStateOf("") }
    var feedbackText by remember { mutableStateOf("") }
    var rating       by remember { mutableFloatStateOf(3f) }
    var submitted    by remember { mutableStateOf(false) }

    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text("Feedback") },
                navigationIcon = { BackButton(navController) },
                colors         = TopAppBarDefaults.topAppBarColors(
                    containerColor    = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            OutlinedTextField(
                value         = employeeName,
                onValueChange = { employeeName = it },
                label         = { Text("Employee Name") },
                modifier      = Modifier.fillMaxWidth(),
                singleLine    = true
            )

            OutlinedTextField(
                value         = feedbackText,
                onValueChange = { feedbackText = it },
                label         = { Text("Your Feedback") },
                minLines      = 3,
                modifier      = Modifier.fillMaxWidth()
            )

            Text(
                text  = "Rating: ${rating.toInt()} / 5",
                style = MaterialTheme.typography.bodyMedium
            )

            Slider(
                value         = rating,
                onValueChange = { rating = it },
                valueRange    = 1f..5f,
                steps         = 3
            )

            Button(
                onClick  = { submitted = true },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp)
            ) {
                Text("Submit Feedback",
                    style = MaterialTheme.typography.titleMedium)
            }

            if (submitted) {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Text(
                        text     = "✅ Thank you for your feedback!",
                        modifier = Modifier.padding(16.dp),
                        style    = MaterialTheme.typography.bodyMedium,
                        color    = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}