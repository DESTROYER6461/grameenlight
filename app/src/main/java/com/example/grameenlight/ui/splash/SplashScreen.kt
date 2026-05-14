package com.example.grameenlight.ui.splash

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(
    onReady: (Boolean, String) -> Unit
) {

    var scaleTarget by remember {

        mutableFloatStateOf(0f)
    }

    val scale by animateFloatAsState(

        targetValue = scaleTarget,

        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy
        ),

        label = "splash-scale"
    )

    val infiniteTransition =
        rememberInfiniteTransition(label = "glow")

    val glowAlpha by infiniteTransition.animateFloat(

        initialValue = 0.5f,

        targetValue = 1f,

        animationSpec = infiniteRepeatable(

            animation = tween(800),

            repeatMode = RepeatMode.Reverse
        ),

        label = "glow-alpha"
    )

    LaunchedEffect(Unit) {

        scaleTarget = 1f

        delay(2200)

        val auth = FirebaseAuth.getInstance()

        val user = auth.currentUser

        // USER LOGGED IN
        if (user != null) {

            FirebaseFirestore.getInstance()
                .collection("users")
                .document(user.uid)
                .get()

                .addOnSuccessListener {

                    val role =
                        it.getString("role") ?: "citizen"

                    onReady(true, role)
                }

                .addOnFailureListener {

                    onReady(true, "citizen")
                }

        }

        // USER NOT LOGGED IN
        else {

            onReady(false, "")
        }
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0A0E1A)),

        contentAlignment = Alignment.Center
    ) {

        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Text(
                text = "💡",

                fontSize = 72.sp,

                modifier = Modifier.scale(scale)
            )

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Grameen-Light",

                fontSize = 28.sp,

                fontWeight = FontWeight.Bold,

                color = Color(0xFFFFC107)
                    .copy(alpha = glowAlpha)
            )

            Spacer(modifier = Modifier.height(6.dp))

            Text(
                text = "Smart Village Energy",

                style = MaterialTheme.typography.bodyLarge,

                color = Color.White.copy(alpha = 0.7f)
            )

            Spacer(modifier = Modifier.height(28.dp))

            CircularProgressIndicator(
                color = Color(0xFFFFC107)
            )

            Spacer(modifier = Modifier.height(48.dp))

            Text(
                text = "Zero Dark Nights · Zero Energy Waste",

                style = MaterialTheme.typography.bodySmall,

                color = Color.White.copy(alpha = 0.4f)
            )
        }
    }
}