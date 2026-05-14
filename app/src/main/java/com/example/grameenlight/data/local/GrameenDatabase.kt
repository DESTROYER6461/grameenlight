package com.example.grameenlight.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ReportEntity::class], version = 1)
abstract class GrameenDatabase : RoomDatabase() {
    abstract fun reportDao(): ReportDao
}