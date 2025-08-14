package com.lasec.monitoreoapp.data.database.entities.incident_entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "incident_reports_table")
data class IncidentReportEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id") val id: Int = 0,

    @ColumnInfo(name = "task_planning_id") val taskPlanningId: Int,
    @ColumnInfo(name = "lost_time") val lostTime: String,
    @ColumnInfo(name = "percentage") val percentage: Double,
    @ColumnInfo(name = "quantity") val quantity: Double,

    @ColumnInfo(name = "category_id") val categoryId: Int,
    @ColumnInfo(name = "sub_category_id") val subCategoryId: Int,
    @ColumnInfo(name = "place_id") val placeId: Int,
    @ColumnInfo(name = "level_id") val levelId: Int,

    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "registered_by_user_id") val registeredByUserId: String,

    @ColumnInfo(name = "has_associated_progress_log") val hasAssociatedProgressLog: Boolean,
    @ColumnInfo(name = "has_scheduled_activity") val hasScheduledActivity: Boolean,
    @ColumnInfo(name = "priority_id") val priorityId: Int
)