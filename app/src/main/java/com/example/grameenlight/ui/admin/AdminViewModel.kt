package com.example.grameenlight.ui.admin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grameenlight.data.model.ElectricityReport
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await

class AdminViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _uiState = MutableStateFlow(AdminUiState())
    val uiState: StateFlow<AdminUiState> = _uiState

    init {
        getReports()
    }

    fun getReports() {

        viewModelScope.launch {

            _uiState.value = _uiState.value.copy(loading = true)

            try {

                val reports = firestore
                    .collection("reports")
                    .get()
                    .await()
                    .toObjects(ElectricityReport::class.java)

                _uiState.value = AdminUiState(
                    reports = reports,
                    loading = false
                )

            } catch (e: Exception) {

                _uiState.value = AdminUiState(
                    error = e.message,
                    loading = false
                )
            }
        }
    }

    fun updateStatus(reportId: String, status: String) {

        viewModelScope.launch {

            firestore
                .collection("reports")
                .document(reportId)
                .update("status", status)
                .await()

            getReports()
        }
    }
}