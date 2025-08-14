package com.lasec.monitoreoapp.data.database.entities.progress_logs_entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "progress_logs_from_tasks_table")
data class ProgressLogEntityFromTasks(
    @PrimaryKey val progressLogId: Int,
    val taskPlanningId: Int,
    val activityTypeDepartmentOriginWorkOrderId: Int,
    val activityStatusId: Int,
    val percentage: Double,
    val quantity: Double,
    val createdAt: String
)
