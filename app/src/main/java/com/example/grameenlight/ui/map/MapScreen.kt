package com.example.grameenlight.ui.map

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.grameenlight.data.model.Pole
import com.example.grameenlight.ui.components.BackButton
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.CameraPosition
import com.google.android.gms.maps.model.LatLng
import com.google.maps.android.compose.GoogleMap
import com.google.maps.android.compose.MapProperties
import com.google.maps.android.compose.MapType
import com.google.maps.android.compose.MapUiSettings
import com.google.maps.android.compose.Marker
import com.google.maps.android.compose.MarkerState
import com.google.maps.android.compose.rememberCameraPositionState

private val KARNATAKA_CENTER = LatLng(15.3173, 75.7139)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MapScreen(
    navController: NavController,
    vm: MapViewModel = viewModel()
) {
    // Fixed: Use vm.filteredPoles instead of vm.poles which doesn't exist in MapViewModel
    val poles     by vm.filteredPoles.collectAsState()
    val isLoading by vm.isLoading.collectAsState()

    var selectedPole by remember { mutableStateOf<Pole?>(null) }
    var showHelp     by remember { mutableStateOf(false) }

    val cameraState = rememberCameraPositionState {
        position = CameraPosition.fromLatLngZoom(KARNATAKA_CENTER, 7f)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text("Karnataka Map") },
                navigationIcon = { BackButton(navController) },
                colors         = TopAppBarDefaults.topAppBarColors(
                    containerColor    = Color(0xFF007BFF),
                    titleContentColor = Color.White
                ),
                actions = {
                    TextButton(onClick = { showHelp = true }) {
                        Text("Help", color = Color.White)
                    }
                }
            )
        }
    ) { padding ->

        Box(
            modifier = Modifier
                .padding(padding)
                .fillMaxSize()
        ) {
            GoogleMap(
                modifier            = Modifier.fillMaxSize(),
                cameraPositionState = cameraState,
                properties          = MapProperties(mapType = MapType.NORMAL),
                uiSettings          = MapUiSettings(zoomControlsEnabled = true)
            ) {
                poles.forEach { pole ->
                    val hue = when (pole.status) {
                        "fused",   "Pending"  ->
                            BitmapDescriptorFactory.HUE_RED
                        "daytime_on", "Assigned" ->
                            BitmapDescriptorFactory.HUE_YELLOW
                        "Fixed", "Working"               ->
                            BitmapDescriptorFactory.HUE_GREEN
                        else                  ->
                            BitmapDescriptorFactory.HUE_GREEN
                    }
                    Marker(
                        state   = MarkerState(
                            position = LatLng(pole.latitude, pole.longitude)
                        ),
                        icon    = BitmapDescriptorFactory.defaultMarker(hue),
                        // Fixed: Use pole.poleId instead of pole.id
                        title   = "Pole ${pole.poleId}",
                        snippet = pole.status,
                        onClick = { selectedPole = pole; false }
                    )
                }
            }

            // Loading indicator
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(48.dp),
                    color = Color(0xFF007BFF)
                )
            }

            // Legend
            Card(
                modifier  = Modifier
                    .align(Alignment.BottomStart)
                    .padding(16.dp),
                shape     = RoundedCornerShape(12.dp),
                elevation = CardDefaults.cardElevation(4.dp)
            ) {
                Column(
                    modifier            = Modifier.padding(12.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text("Legend",
                        style      = MaterialTheme.typography.labelMedium,
                        fontWeight = FontWeight.Bold)
                    LegendItem(Color(0xFF2E7D32), "Working / Fixed")
                    LegendItem(Color(0xFFE53935), "Fused / Pending")
                    LegendItem(Color(0xFFFFA000), "Daytime ON / Assigned")
                }
            }

            // Pole count
            Card(
                modifier = Modifier
                    .align(Alignment.TopEnd)
                    .padding(16.dp),
                shape    = RoundedCornerShape(20.dp),
                colors   = CardDefaults.cardColors(
                    containerColor = Color(0xFF007BFF).copy(alpha = 0.9f)
                )
            ) {
                Text(
                    "${poles.size} poles",
                    modifier = Modifier.padding(
                        horizontal = 12.dp, vertical = 6.dp),
                    style    = MaterialTheme.typography.bodySmall,
                    color    = Color.White,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }

    // Pole detail dialog
    selectedPole?.let { pole ->
        val statusColor = when (pole.status) {
            "Fixed", "Working" -> Color(0xFF2E7D32)
            "Assigned"         -> Color(0xFFFFA000)
            "Pending", "fused" -> Color(0xFFE53935)
            else               -> Color(0xFF2E7D32)
        }
        AlertDialog(
            onDismissRequest = { selectedPole = null },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    // Fixed: Use pole.poleId instead of pole.id
                    Text("Pole ${pole.poleId}",
                        style = MaterialTheme.typography.titleMedium)
                }
            },
            text = {
                Column(
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = statusColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            pole.status,
                            modifier   = Modifier.padding(
                                horizontal = 10.dp, vertical = 4.dp),
                            color      = statusColor,
                            fontWeight = FontWeight.Bold,
                            style      = MaterialTheme.typography.bodySmall
                        )
                    }
                    Text("Lat  : ${"%.4f".format(pole.latitude)}")
                    Text("Long : ${"%.4f".format(pole.longitude)}")
                }
            },
            confirmButton = {
                TextButton(onClick = { selectedPole = null }) {
                    Text("Close")
                }
            }
        )
    }

    // Help dialog
    if (showHelp) {
        AlertDialog(
            onDismissRequest = { showHelp = false },
            title  = { Text("Helpline") },
            text   = {
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    Text("Karnataka Power Corporation Ltd (KPCL)")
                    Text("📞 1912  (24×7 toll-free)")
                    Text("📞 1800-425-9919 (BESCOM)")
                    Text("\nTap any marker to view pole details.")
                    Text("🔴 Red    = Fused / Pending")
                    Text("🟡 Yellow = Daytime ON / Assigned")
                    Text("🟢 Green  = Working / Fixed")
                }
            },
            confirmButton = {
                TextButton(onClick = { showHelp = false }) { Text("OK") }
            }
        )
    }
}

@Composable
fun LegendItem(color: Color, label: String) {
    Row(
        verticalAlignment     = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Surface(
            modifier = Modifier.size(12.dp),
            shape    = MaterialTheme.shapes.small,
            color    = color
        ) {}
        Text(label, style = MaterialTheme.typography.bodySmall)
    }
}
