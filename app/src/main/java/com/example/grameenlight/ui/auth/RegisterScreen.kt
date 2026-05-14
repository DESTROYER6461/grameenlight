package com.example.grameenlight.ui.auth

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.navigation.NavController
import com.example.grameenlight.ui.components.BackButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    navController: NavController,
    onSuccess: () -> Unit
) {
    var name        by remember { mutableStateOf("") }
    var phone       by remember { mutableStateOf("") }
    var email       by remember { mutableStateOf("") }
    var password    by remember { mutableStateOf("") }
    var confirmPass by remember { mutableStateOf("") }
    var isLoading   by remember { mutableStateOf(false) }
    var errorMsg    by remember { mutableStateOf("") }
    var successMsg  by remember { mutableStateOf("") }

    val scope = rememberCoroutineScope()
    val auth  = FirebaseAuth.getInstance()
    val db    = FirebaseFirestore.getInstance()

    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text("Create Account") },
                navigationIcon = { BackButton(navController) },
                colors         = TopAppBarDefaults.topAppBarColors(
                    containerColor    = MaterialTheme.colorScheme.primary,
                    titleContentColor = MaterialTheme.colorScheme.onPrimary
                )
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color(0xFFF5F7FA),
                            Color(0xFFE8F0FE)
                        )
                    )
                )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .verticalScroll(rememberScrollState()),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                Spacer(Modifier.height(8.dp))

                // Header
                Text("💡", fontSize = 48.sp)
                Text(
                    "Join Grameen-Light",
                    fontSize   = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color      = Color(0xFF007BFF)
                )
                Text(
                    "Register to report streetlight issues",
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Gray
                )

                Spacer(Modifier.height(8.dp))

                // Form card
                Card(
                    shape  = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = Color.White
                    ),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier            = Modifier.padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(14.dp)
                    ) {

                        Text(
                            "Personal Details",
                            style      = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedTextField(
                            value         = name,
                            onValueChange = { name = it },
                            label         = { Text("Full Name *") },
                            modifier      = Modifier.fillMaxWidth(),
                            singleLine    = true,
                            shape         = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value         = phone,
                            onValueChange = {
                                // only allow digits, max 10
                                if (it.length <= 10 && it.all { c -> c.isDigit() })
                                    phone = it
                            },
                            label         = { Text("Phone Number *") },
                            modifier      = Modifier.fillMaxWidth(),
                            singleLine    = true,
                            shape         = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value         = email,
                            onValueChange = { email = it },
                            label         = { Text("Email Address *") },
                            modifier      = Modifier.fillMaxWidth(),
                            singleLine    = true,
                            shape         = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value                = password,
                            onValueChange        = { password = it },
                            label                = { Text("Password * (min 6 chars)") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier             = Modifier.fillMaxWidth(),
                            singleLine           = true,
                            shape                = RoundedCornerShape(12.dp)
                        )

                        OutlinedTextField(
                            value                = confirmPass,
                            onValueChange        = { confirmPass = it },
                            label                = { Text("Confirm Password *") },
                            visualTransformation = PasswordVisualTransformation(),
                            modifier             = Modifier.fillMaxWidth(),
                            singleLine           = true,
                            shape                = RoundedCornerShape(12.dp),
                            isError              = confirmPass.isNotEmpty()
                                    && confirmPass != password
                        )

                        if (confirmPass.isNotEmpty() && confirmPass != password) {
                            Text(
                                "Passwords do not match",
                                color = MaterialTheme.colorScheme.error,
                                style = MaterialTheme.typography.bodySmall
                            )
                        }

                        // Error message
                        if (errorMsg.isNotEmpty()) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = MaterialTheme.colorScheme.errorContainer
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text     = "⚠️ $errorMsg",
                                    modifier = Modifier.padding(12.dp),
                                    color    = MaterialTheme.colorScheme.onErrorContainer,
                                    style    = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        // Success message
                        if (successMsg.isNotEmpty()) {
                            Card(
                                colors = CardDefaults.cardColors(
                                    containerColor = Color(0xFFE8F5E9)
                                ),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text     = "✅ $successMsg",
                                    modifier = Modifier.padding(12.dp),
                                    color    = Color(0xFF2E7D32),
                                    style    = MaterialTheme.typography.bodySmall
                                )
                            }
                        }

                        // Register button
                        Button(
                            onClick = {
                                // Validate
                                errorMsg = when {
                                    name.isBlank() ->
                                        "Please enter your full name."
                                    phone.length < 10 ->
                                        "Please enter a valid 10-digit phone number."
                                    email.isBlank() || !email.contains("@") ->
                                        "Please enter a valid email address."
                                    password.length < 6 ->
                                        "Password must be at least 6 characters."
                                    password != confirmPass ->
                                        "Passwords do not match."
                                    else -> ""
                                }

                                if (errorMsg.isNotEmpty()) return@Button

                                scope.launch {
                                    isLoading = true
                                    errorMsg  = ""
                                    try {
                                        // 1. Create Firebase Auth account
                                        val result = auth
                                            .createUserWithEmailAndPassword(
                                                email.trim(),
                                                password.trim()
                                            ).await()

                                        val uid = result.user?.uid ?: ""

                                        // 2. Save user details to Firestore
                                        db.collection("users")
                                            .document(uid)
                                            .set(
                                                mapOf(
                                                    "id"    to uid,
                                                    "name"  to name.trim(),
                                                    "phone" to phone.trim(),
                                                    "email" to email.trim(),
                                                    "role"  to "user"
                                                )
                                            ).await()

                                        successMsg = "Account created! " +
                                                "Redirecting to login..."

                                        // 3. Sign out so user logs in fresh
                                        auth.signOut()

                                        // 4. Short delay then go back to login
                                        kotlinx.coroutines.delay(1500)
                                        onSuccess()

                                    } catch (e: Exception) {
                                        errorMsg = when {
                                            e.message?.contains(
                                                "email address is already"
                                            ) == true ->
                                                "This email is already registered. " +
                                                        "Please login instead."
                                            e.message?.contains("network") == true ->
                                                "No internet connection. " +
                                                        "Please check your network."
                                            else ->
                                                "Registration failed: ${e.message}"
                                        }
                                    } finally {
                                        isLoading = false
                                    }
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(54.dp),
                            enabled = !isLoading,
                            shape   = RoundedCornerShape(12.dp),
                            colors  = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFF007BFF)
                            )
                        ) {
                            if (isLoading) {
                                CircularProgressIndicator(
                                    modifier    = Modifier.size(22.dp),
                                    color       = Color.White,
                                    strokeWidth = 2.dp
                                )
                            } else {
                                Text(
                                    "Create Account",
                                    fontSize   = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color      = Color.White
                                )
                            }
                        }
                    }
                }

                // Already have account
                TextButton(
                    onClick = { navController.popBackStack() }
                ) {
                    Text(
                        "Already have an account? Login",
                        color = Color(0xFF007BFF)
                    )
                }

                Spacer(Modifier.height(24.dp))
            }
        }
    }
}