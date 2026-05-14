package com.example.grameenlight.data.model

data class PoleMarker(
    val poleId: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val status: String = "Pending",
    val issue: String = ""
)
