package com.example.grameenlight.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "reports")
data class ReportEntity(
    @PrimaryKey val id: String,
    val issueType: String,
    val imageUrl: String,
    val latitude: Double,
    val longitude: Double,
    val village: String,
    val status: String,
    val timestamp: Long,
    val synced: Boolean = false
)