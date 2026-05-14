package com.example.grameenlight

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.navigation.compose.rememberNavController
import com.example.grameenlight.navigation.GrameenNavGraph
import com.example.grameenlight.ui.theme.GrameenLightTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GrameenLightTheme {
                val navController = rememberNavController()
                GrameenNavGraph(navController = navController)
            }
        }
    }
}