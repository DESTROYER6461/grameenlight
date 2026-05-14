package com.example.grameenlight.data.model

import com.google.firebase.firestore.GeoPoint

data class ElectricityReport(
    val reportId: String = "",
    val userId: String = "",

    // Issue Details
    val issueType: String = "",
    val issueTitle: String = "",
    val description: String = "",

    // Priority
    val priority: String = "Medium",

    // Administrative Location
    val district: String = "",
    val taluk: String = "",
    val panchayatName: String = "",
    val villageName: String = "",
    val wardName: String = "",

    // Address Details
    val address: String = "",
    val landmark: String = "",
    val poleNumber: String = "",

    // GPS Location
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,

    // Image
    val imageUrl: String = "",

    // Status
    val status: String = "Pending",

    // Assigned Technician
    val assignedTo: String = "",

    // Emergency
    val isEmergency: Boolean = false,

    // Danger Level
    val dangerLevel: String = "",

    // Time
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis()
)
