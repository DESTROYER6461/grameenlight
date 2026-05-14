package com.example.grameenlight.ui.report

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.grameenlight.data.model.ElectricityReport
import com.example.grameenlight.data.repository.FirestoreRepository
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.launch

class ReportViewModel : ViewModel() {

    private val repository = FirestoreRepository(FirebaseFirestore.getInstance())

    fun submitReport(report: ElectricityReport) {
        viewModelScope.launch {
            repository.submitElectricityReport(report)
        }
    }
}
