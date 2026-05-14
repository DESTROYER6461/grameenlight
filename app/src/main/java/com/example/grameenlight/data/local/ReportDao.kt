package com.example.grameenlight.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ReportDao {
    @Query("SELECT * FROM reports ORDER BY timestamp DESC")
    fun getAllReports(): Flow<List<ReportEntity>>

    @Query("SELECT * FROM reports WHERE synced = 0")
    suspend fun getUnsyncedReports(): List<ReportEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReport(report: ReportEntity)

    @Query("UPDATE reports SET status = :status, synced = 1 WHERE id = :id")
    suspend fun updateStatus(id: String, status: String)
}