package com.example.grameenlight.ui.admin

import com.example.grameenlight.data.model.ElectricityReport

data class AdminUiState(
    val reports: List<ElectricityReport> = emptyList(),
    val loading: Boolean = false,
    val error: String? = null
)