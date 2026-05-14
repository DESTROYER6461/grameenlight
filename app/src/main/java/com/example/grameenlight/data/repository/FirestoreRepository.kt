package com.example.grameenlight.data.repository

import com.example.grameenlight.data.model.*
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import kotlinx.coroutines.channels.awaitClose
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.callbackFlow
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

class FirestoreRepository @Inject constructor(
    private val db: FirebaseFirestore
) {
    // Generate a short complaint ID like GL-XXXX
    fun generateComplaintId(): String =
        "GL-" + UUID.randomUUID().toString().take(6).uppercase()

    // Submit a new report (Report model)
    suspend fun submitReport(report: Report) {
        db.collection("reports").document(report.id).set(report).await()
    }

    // Listen to all reports in real time (Report model)
    fun getAllReports(): Flow<List<Report>> = callbackFlow {
        val sub = db.collection("reports")
            .orderBy("timestamp", Query.Direction.DESCENDING)
            .addSnapshotListener { snap, _ ->
                val list = snap?.toObjects(Report::class.java) ?: emptyList()
                trySend(list)
            }
        awaitClose { sub.remove() }
    }

    // Get reports for a specific user
    fun getUserReports(userId: String): Flow<List<Report>> = callbackFlow {
        val sub = db.collection("reports")
            .whereEqualTo("userId", userId)
            .addSnapshotListener { snap, _ ->
                trySend(snap?.toObjects(Report::class.java) ?: emptyList())
            }
        awaitClose { sub.remove() }
    }

    // Admin: update complaint status
    suspend fun updateReportStatus(reportId: String, status: String) {
        db.collection("reports").document(reportId).update("status", status).await()
    }

    // Get poles for Karnataka map
    fun getPolesForKarnataka(): Flow<List<Pole>> = callbackFlow {
        val sub = db.collection("poles")
            .whereEqualTo("state", "Karnataka")
            .addSnapshotListener { snap, _ ->
                trySend(snap?.toObjects(Pole::class.java) ?: emptyList())
            }
        awaitClose { sub.remove() }
    }

    // Submit feedback
    suspend fun submitFeedback(feedback: Feedback) {
        db.collection("feedback").document(feedback.id).set(feedback).await()
    }

    // ElectricityReport specific methods (Renamed to avoid conflicting overloads)
    suspend fun submitElectricityReport(report: ElectricityReport): Boolean {
        return try {
            db.collection("reports")
                .document(report.reportId)
                .set(report)
                .await()
            true
        } catch (e: Exception) {
            false
        }
    }

    suspend fun getAllElectricityReports(): List<ElectricityReport> {
        return try {
            db.collection("reports")
                .get()
                .await()
                .toObjects(ElectricityReport::class.java)
        } catch (e: Exception) {
            emptyList()
        }
    }
}
