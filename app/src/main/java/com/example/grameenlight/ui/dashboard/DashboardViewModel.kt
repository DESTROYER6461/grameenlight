package com.example.grameenlight.ui.dashboard

import androidx.lifecycle.ViewModel
import com.example.grameenlight.data.model.DashboardStats
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class DashboardViewModel : ViewModel() {

    private val firestore = FirebaseFirestore.getInstance()

    private val _stats = MutableStateFlow(DashboardStats())
    val stats: StateFlow<DashboardStats> = _stats

    init {
        listenToReports()
    }

    private fun listenToReports() {
        firestore.collection("reports")
            .addSnapshotListener { snapshot, error ->
                if (error != null || snapshot == null) {
                    return@addSnapshotListener
                }

                val reports = snapshot.documents
                val total = reports.size

                val pending = reports.count {
                    it.getString("status") == "Pending"
                }

                val assigned = reports.count {
                    it.getString("status") == "Assigned"
                }

                val working = reports.count {
                    it.getString("status") == "Working"
                }

                val resolved = reports.count {
                    it.getString("status") == "Resolved"
                }

                val energySaved = resolved * 8

                _stats.value = DashboardStats(
                    total = total,
                    pending = pending,
                    assigned = assigned,
                    working = working,
                    resolved = resolved,
                    energySaved = energySaved
                )
            }
    }
}
