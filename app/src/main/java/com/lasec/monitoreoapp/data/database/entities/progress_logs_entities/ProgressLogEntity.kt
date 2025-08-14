package com.lasec.monitoreoapp.data.database.entities.progress_logs_entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progress_logs_table")
data class ProgressLogEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,

    @ColumnInfo(name = "quantity") val quantity: Double,
    @ColumnInfo(name = "task_planning_id") val taskPlanningId: Int,
    @ColumnInfo(name = "percentage") val percentage: Double,
    @ColumnInfo(name = "activity_status_id") val activityStatusId: Int
)