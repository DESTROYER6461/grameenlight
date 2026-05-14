package com.example.grameenlight.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

const val ADMIN_EMAIL = "admin@grameenlight.com"

@Composable
fun LoginScreen(
    onLoginSuccess: (String) -> Unit,
    onRegister: () -> Unit
) {
    var email       by remember { mutableStateOf("") }
    var password    by remember { mutableStateOf("") }
    var isAdmin     by remember { mutableStateOf(false) }
    var isLoading   by remember { mutableStateOf(false) }
    var errorMsg    by remember { mutableStateOf("") }
    val scope       = rememberCoroutineScope()
    val auth        = FirebaseAuth.getInstance()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(
                Brush.verticalGradient(
                    colors = listOf(Color(0xFF0A0E1A), Color(0xFF1A237E))
                )
            )
    ) {
        Column(
            modifier              = Modifier
                .fillMaxSize()
                .padding(24.dp),
            verticalArrangement   = Arrangement.Center,
            horizontalAlignment   = Alignment.CenterHorizontally
        ) {
            Text("💡", fontSize = 64.sp)
            Spacer(Modifier.height(8.dp))
            Text(
                "Grameen-Light",
                fontSize   = 28.sp,
                fontWeight = FontWeight.Bold,
                color      = Color(0xFFFFC107)
            )
            Text(
                "Smart Village Energy",
                style = MaterialTheme.typography.bodyMedium,
                color = Color.White.copy(alpha = 0.7f)
            )
            Spacer(Modifier.height(32.dp))

            Card(
                shape  = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = Color.White.copy(alpha = 0.95f)
                )
            ) {
                Column(
                    modifier            = Modifier.padding(24.dp),
                    verticalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    Text(
                        if (isAdmin) "Admin Login" else "Citizen Login",
                        style      = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color      = Color(0xFF0A0E1A)
                    )

                    OutlinedTextField(
                        value         = email,
                        onValueChange = { email = it },
                        label         = { Text("Email") },
                        modifier      = Modifier.fillMaxWidth(),
                        singleLine    = true
                    )

                    OutlinedTextField(
                        value               = password,
                        onValueChange       = { password = it },
                        label               = { Text("Password") },
                        visualTransformation = PasswordVisualTransformation(),
                        modifier            = Modifier.fillMaxWidth(),
                        singleLine          = true
                    )

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Checkbox(
                            checked           = isAdmin,
                            onCheckedChange   = {
                                isAdmin  = it
                                if (it) {
                                    email    = "admin@grameenlight.com"
                                    password = "Admin@123"
                                } else {
                                    email    = ""
                                    password = ""
                                }
                            }
                        )
                        Text("Login as Admin (Panchayat)")
                    }

                    if (errorMsg.isNotEmpty()) {
                        Text(
                            text  = errorMsg,
                            color = MaterialTheme.colorScheme.error,
                            style = MaterialTheme.typography.bodySmall
                        )
                    }

                    Button(
                        onClick = {
                            if (email.isBlank() || password.isBlank()) {
                                errorMsg = "Please enter email and password."
                                return@Button
                            }
                            scope.launch {
                                isLoading = true
                                errorMsg  = ""
                                try {
                                    auth.signInWithEmailAndPassword(
                                        email.trim(), password.trim()
                                    ).await()
                                    val role = if (
                                        email.trim() == ADMIN_EMAIL
                                    ) "admin" else "user"
                                    onLoginSuccess(role)
                                } catch (e: Exception) {
                                    errorMsg = "Login failed: ${e.message}"
                                } finally {
                                    isLoading = false
                                }
                            }
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        enabled  = !isLoading,
                        colors   = ButtonDefaults.buttonColors(
                            containerColor = Color(0xFF007BFF)
                        )
                    ) {
                        if (isLoading) {
                            CircularProgressIndicator(
                                modifier    = Modifier.size(20.dp),
                                color       = Color.White,
                                strokeWidth = 2.dp
                            )
                        } else {
                            Text(
                                "Login",
                                style = MaterialTheme.typography.titleMedium,
                                color = Color.White
                            )
                        }
                    }

                    if (!isAdmin) {
                        TextButton(
                            onClick  = onRegister,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Text("Don't have an account? Register")
                        }
                    }
                }
            }

            Spacer(Modifier.height(24.dp))

            // Admin hint card
            if (isAdmin) {
                Card(
                    shape  = RoundedCornerShape(8.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color(0xFFFFF9C4)
                    )
                ) {
                    Column(Modifier.padding(12.dp)) {
                        Text(
                            "Admin Credentials",
                            fontWeight = FontWeight.Bold,
                            color      = Color(0xFF5D4037),
                            style      = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            "Email    : admin@grameenlight.com",
                            color = Color(0xFF5D4037),
                            style = MaterialTheme.typography.bodySmall
                        )
                        Text(
                            "Password : Admin@123",
                            color = Color(0xFF5D4037),
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }
            }
        }
    }
}