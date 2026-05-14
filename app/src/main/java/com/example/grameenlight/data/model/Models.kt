package com.example.grameenlight.data.model

import com.google.firebase.Timestamp

data class User(
    val id           : String    = "",
    val name         : String    = "",
    val phone        : String    = "",
    val email        : String    = "",
    val profileImage : String    = "",
    val role         : String    = "user"
)

data class Report(
    val id          : String    = "",
    val userId      : String    = "",
    val poleId      : String    = "",
    val issueType   : String    = "",
    val imageUrl    : String    = "",
    val latitude    : Double    = 0.0,
    val longitude   : Double    = 0.0,
    val village     : String    = "",
    val city        : String    = "",
    val district    : String    = "",
    val state       : String    = "Karnataka",
    val description : String    = "",
    val landmark    : String    = "",
    val status      : String    = "Pending",
    val timestamp   : Timestamp = Timestamp.now()
)

data class Feedback(
    val id           : String    = "",
    val userId       : String    = "",
    val employeeName : String    = "",
    val feedback     : String    = "",
    val rating       : Float     = 0f,
    val timestamp    : Timestamp = Timestamp.now()
)

data class Pole(
    val poleId: String = "",
    val latitude: Double = 0.0,
    val longitude: Double = 0.0,
    val status: String = "Working", // Working, Pending, Assigned, Faulty
    val type: String = "Street Light", // Village Use, Agriculture Use, Street Light, Transformer, Water Supply
    val village: String = "",
    val panchayat: String = "",
    val streetName: String = "",
    val issue: String = "",
    val imageUrl: String = "",
    val assignedWorker: String = "",
    val installYear: Int = 2024
)
