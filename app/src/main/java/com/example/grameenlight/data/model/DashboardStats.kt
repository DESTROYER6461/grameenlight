package com.example.grameenlight.data.model

data class DashboardStats(
    val total: Int = 0,
    val pending: Int = 0,
    val assigned: Int = 0,
    val working: Int = 0,
    val resolved: Int = 0,
    val energySaved: Int = 0
)
