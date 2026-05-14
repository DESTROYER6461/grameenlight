package com.example.grameenlight.ui.profile

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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.grameenlight.navigation.Screen
import com.example.grameenlight.ui.components.BackButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(navController: NavController) {

    val auth = FirebaseAuth.getInstance()
    val db   = FirebaseFirestore.getInstance()
    val uid  = auth.currentUser?.uid ?: ""

    var userName     by remember { mutableStateOf("Loading...") }
    var userEmail    by remember { mutableStateOf("") }
    var userPhone    by remember { mutableStateOf("") }
    var reportCount  by remember { mutableIntStateOf(0) }
    var resolvedCount by remember { mutableIntStateOf(0) }
    var feedbackCount by remember { mutableIntStateOf(0) }

    // Load user data from Firestore
    LaunchedEffect(uid) {
        if (uid.isNotEmpty()) {
            db.collection("users").document(uid).get()
                .addOnSuccessListener { doc ->
                    userName  = doc.getString("name")  ?: "User"
                    userEmail = doc.getString("email") ?: auth.currentUser?.email ?: ""
                    userPhone = doc.getString("phone") ?: ""
                }

            db.collection("reports")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener { snap ->
                    reportCount   = snap.size()
                    resolvedCount = snap.documents.count {
                        it.getString("status") == "Fixed"
                    }
                }

            db.collection("feedback")
                .whereEqualTo("userId", uid)
                .get()
                .addOnSuccessListener { snap ->
                    feedbackCount = snap.size()
                }
        } else {
            // Admin or anonymous
            userEmail = auth.currentUser?.email ?: ""
            userName  = if (userEmail == "admin@grameenlight.com")
                "Admin" else "User"
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text("My Profile") },
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            // ── Header gradient banner ────────────────────────────────────
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF007BFF),
                                Color(0xFF0056CC)
                            )
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Box(
                        modifier         = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(Color.White.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text("👤", fontSize = 40.sp)
                    }
                    Spacer(Modifier.height(8.dp))
                    Text(
                        userName,
                        fontWeight = FontWeight.Bold,
                        fontSize   = 20.sp,
                        color      = Color.White
                    )
                    Text(
                        userEmail,
                        fontSize = 13.sp,
                        color    = Color.White.copy(alpha = 0.8f)
                    )
                }
            }

            Column(
                modifier            = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // ── Stats row ─────────────────────────────────────────────
                Card(
                    shape  = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.primaryContainer
                    )
                ) {
                    Row(
                        modifier              = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        horizontalArrangement = Arrangement.SpaceEvenly
                    ) {
                        StatItem("$reportCount",   "Reports",  Color(0xFF007BFF))
                        StatItem("$resolvedCount", "Resolved", Color(0xFF2E7D32))
                        StatItem("$feedbackCount", "Feedback", Color(0xFFFFA000))
                    }
                }

                // ── Info card ─────────────────────────────────────────────
                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(
                        modifier            = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        Text(
                            "Account Info",
                            style      = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        InfoRow(Icons.Filled.Person, "Name",  userName)
                        InfoRow(Icons.Filled.Email,  "Email", userEmail)
                        if (userPhone.isNotEmpty()) {
                            InfoRow(Icons.Filled.Phone, "Phone", userPhone)
                        }
                    }
                }

                // ── Action buttons ────────────────────────────────────────
                Card(shape = RoundedCornerShape(16.dp)) {
                    Column(
                        modifier            = Modifier.padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(10.dp)
                    ) {
                        Text(
                            "Account Settings",
                            style      = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )

                        OutlinedButton(
                            onClick  = { },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Filled.Edit, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Edit Profile")
                        }

                        OutlinedButton(
                            onClick  = { },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Icon(Icons.Filled.Lock, contentDescription = null)
                            Spacer(Modifier.width(8.dp))
                            Text("Change Password")
                        }
                    }
                }

                // ── Logout ────────────────────────────────────────────────
                Button(
                    onClick = {
                        auth.signOut()
                        navController.navigate(Screen.Login.route) {
                            popUpTo(0) { inclusive = true }
                        }
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp),
                    shape  = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.error
                    )
                ) {
                    Text(
                        "Logout",
                        style = MaterialTheme.typography.titleMedium,
                        color = Color.White
                    )
                }

                Spacer(Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun StatItem(value: String, label: String, color: Color) {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            value,
            fontSize   = 24.sp,
            fontWeight = FontWeight.Bold,
            color      = color
        )
        Text(
            label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
fun InfoRow(icon: ImageVector, label: String, value: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier          = Modifier.fillMaxWidth()
    ) {
        Icon(
            imageVector        = icon,
            contentDescription = label,
            tint               = MaterialTheme.colorScheme.primary,
            modifier           = Modifier.size(20.dp)
        )
        Spacer(Modifier.width(12.dp))
        Column {
            Text(
                label,
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Text(
                value,
                style      = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.Medium
            )
        }
    }
}