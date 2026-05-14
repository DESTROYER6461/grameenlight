package com.example.grameenlight.ui.report

import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material.icons.outlined.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.grameenlight.data.model.ElectricityReport
import com.example.grameenlight.ui.components.BackButton
import java.util.UUID
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.outlined.Bolt
import androidx.compose.material.icons.outlined.Lightbulb
import androidx.compose.material.icons.outlined.Power
import androidx.compose.material.icons.outlined.Warning

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReportScreen(
    navController: NavController,
    viewModel: ReportViewModel = viewModel()
) {

    val green = Color(0xFF2E7D32)
    val lightGreen = Color(0xFFEAF4EA)
    var description by remember { mutableStateOf("") }

    var district by remember { mutableStateOf("Kalaburagi") }
    var taluk by remember { mutableStateOf("Aland") }
    var panchayatName by remember { mutableStateOf("Keri Ambalaga Panchayat") }
    var villageName by remember { mutableStateOf("Keri Village") }

    var poleNumber by remember { mutableStateOf("KA-KERI-001") }

    var imageUri by remember { mutableStateOf<Uri?>(null) }

    val imagePicker =
        rememberLauncherForActivityResult(
            contract = ActivityResultContracts.GetContent()
        ) {
            imageUri = it
        }
    var issueType by remember {
        mutableStateOf("Wire Cut/Spark")
    }

    var customIssue by remember {
        mutableStateOf("")
    }

    var poleExpanded by remember {
        mutableStateOf(false)
    }

    val poleList = listOf(

        "KA-KERI-001",
        "KA-KERI-002",
        "KA-KERI-003",
        "KA-KERI-004",
        "KA-KERI-005",
        "KA-KERI-006",
        "KA-KERI-007",
        "KA-KERI-008",
        "KA-KERI-009",
        "KA-KERI-010",
        "KA-KERI-011",
        "KA-KERI-012",
        "KA-KERI-013",
        "KA-KERI-014",
        "KA-KERI-015"
    )

    var selectedPole by remember {

        mutableStateOf("KA-KERI-001")
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title          = { Text("Report Issue") },
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
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {

            // Top Pole Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = lightGreen
                )
            ) {

                Column(
                    modifier = Modifier.padding(16.dp)
                ) {

                    Row(
                        verticalAlignment = Alignment.CenterVertically
                    ) {

                        Box(
                            modifier = Modifier
                                .size(45.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(green),

                            contentAlignment = Alignment.Center
                        ) {

                            Icon(
                                imageVector = Icons.Default.Bolt,
                                contentDescription = null,
                                tint = Color.White
                            )
                        }

                        Spacer(modifier = Modifier.width(12.dp))

                        Column {

                            Text(
                                text = "TARGET INFRASTRUCTURE",
                                fontSize = 10.sp,
                                color = Color.Gray
                            )

                            Text(
                                text = selectedPole,
                                fontWeight = FontWeight.Bold,
                                color = green
                            )

                            Text(
                                text = "Keri Ambalaga Panchayat",
                                fontSize = 12.sp
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    ExposedDropdownMenuBox(
                        expanded = poleExpanded,
                        onExpandedChange = {
                            poleExpanded = !poleExpanded
                        }
                    ) {

                        OutlinedTextField(
                            value = selectedPole,
                            onValueChange = {},
                            readOnly = true,

                            label = {
                                Text("Select Electric Pole")
                            },

                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(
                                    expanded = poleExpanded
                                )
                            },

                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth()
                        )

                        ExposedDropdownMenu(
                            expanded = poleExpanded,
                            onDismissRequest = {
                                poleExpanded = false
                            }
                        ) {

                            poleList.forEach { pole ->

                                DropdownMenuItem(
                                    text = {
                                        Text(pole)
                                    },

                                    onClick = {

                                        selectedPole = pole

                                        poleExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "What is the issue?",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                IssueCard(
                    title = "Wire Cut/Spark",
                    icon = Icons.Outlined.Warning,
                    selected = issueType == "Wire Cut/Spark",
                    green = green
                ) {
                    issueType = "Wire Cut/Spark"
                }

                IssueCard(
                    title = "No Power",
                    icon = Icons.Outlined.Power,
                    selected = issueType == "No Power",
                    green = green
                ) {
                    issueType = "No Power"
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {

                IssueCard(
                    title = "Pole Tilted",
                    icon = Icons.Outlined.Warning,
                    selected = issueType == "Pole Tilted",
                    green = green
                ) {
                    issueType = "Pole Tilted"
                }

                IssueCard(
                    title = "Light Failure",
                    icon = Icons.Outlined.Lightbulb,
                    selected = issueType == "Light Failure",
                    green = green
                ) {
                    issueType = "Light Failure"
                }

            }

            Spacer(modifier = Modifier.height(12.dp))

            Row {

                IssueCard(
                    title = "Other Issue",
                    icon = Icons.Default.Edit,
                    selected = issueType == "Other",
                    green = green
                ) {
                    issueType = "Other"
                }

                Spacer(modifier = Modifier.weight(1f))
            }
            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Upload Photo (Proof)",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(12.dp))

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(160.dp)
                    .border(
                        1.dp,
                        Color.LightGray,
                        RoundedCornerShape(14.dp)
                    )
                    .clickable {
                        imagePicker.launch("image/*")
                    },
                contentAlignment = Alignment.Center
            ) {

                if (imageUri != null) {

                    AsyncImage(
                        model = imageUri,
                        contentDescription = null,
                        modifier = Modifier
                            .fillMaxSize()
                            .clip(RoundedCornerShape(14.dp)),
                        contentScale = ContentScale.Crop
                    )

                } else {

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {

                        Icon(
                            imageVector = Icons.Default.CameraAlt,
                            contentDescription = null,
                            modifier = Modifier.size(36.dp)
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = "Click to capture or upload",
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Additional Details",
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            OutlinedTextField(
                value = description,
                onValueChange = {
                    description = it
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp),
                placeholder = {
                    Text("Describe the issue or add landmarks nearby...")
                },
                shape = RoundedCornerShape(14.dp)
            )

            Spacer(modifier = Modifier.height(20.dp))

            Card(
                modifier = Modifier.fillMaxWidth(),
                border = BorderStroke(
                    1.dp,
                    Color(0xFFD6E7D6)
                ),
                colors = CardDefaults.cardColors(
                    containerColor = Color(0xFFF7FBF7)
                )
            ) {

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {

                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = green
                    )

                    Spacer(modifier = Modifier.width(10.dp))

                    Column {

                        Text(
                            text = "AUTO-GEOTAGGING ACTIVE",
                            color = green,
                            fontWeight = FontWeight.Bold,
                            fontSize = 11.sp
                        )

                        Text(
                            text = "$villageName, $taluk",
                            fontSize = 12.sp
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(30.dp))

            Button(
                onClick = {

                    val report = ElectricityReport(
                        reportId = UUID.randomUUID().toString(),
                        issueType = issueType,
                        issueTitle = issueType,
                        description = description,
                        district = district,
                        taluk = taluk,
                        panchayatName = panchayatName,
                        villageName = villageName,
                        wardName = "",
                        address = "",
                        landmark = "",
                        poleNumber = poleNumber,
                        priority = "Medium",
                        isEmergency = false
                    )

                    viewModel.submitReport(report)

                    navController.popBackStack()
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(14.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = green
                )
            ) {

                Text(
                    text = "Submit Report",
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.width(8.dp))

                Icon(
                    imageVector = Icons.Default.Send,
                    contentDescription = null
                )
            }
        }
    }
}

@Composable
fun RowScope.IssueCard(
    title: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    selected: Boolean,
    green: Color,
    onClick: () -> Unit
) {

    Card(
        modifier = Modifier
            .weight(1f)
            .height(95.dp)
            .clickable {
                onClick()
            },
        border = BorderStroke(
            if (selected) 2.dp else 1.dp,
            if (selected) green else Color.LightGray
        ),
        colors = CardDefaults.cardColors(
            containerColor =
                if (selected) Color(0xFFF1FAF1)
                else Color.White
        ),
        shape = RoundedCornerShape(14.dp)
    ) {

        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Icon(
                imageVector = icon,
                contentDescription = null,
                tint = if (selected) green else Color.Black
            )

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = title,
                fontSize = 12.sp
            )
        }
    }
}
