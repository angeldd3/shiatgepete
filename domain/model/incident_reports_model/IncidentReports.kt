package com.lasec.monitoreoapp.domain.model.incident_reports_model

import com.google.gson.annotations.SerializedName
import com.lasec.monitoreoapp.data.database.entities.incident_entities.IncidentReportEntity

data class IncidentReportsDto(
    @SerializedName("TaskPlanningId") val taskPlanningId: Int,
    @SerializedName("LostTime") val lostTime: String,
    @SerializedName("Percentage") val percentage: Double,
    @SerializedName("Quantity") val quantity: Double,
    @SerializedName("CategoryId") val categoryId: Int,
    @SerializedName("SubCategoryId") val subCategoryId: Int,
    @SerializedName("PlaceId") val placeId: Int,
    @SerializedName("LevelId") val levelId: Int,
    @SerializedName("Name") val name: String,
    @SerializedName("Description") val description: String,
    @SerializedName("RegisteredByUserId") val registeredByUserId: String,
    @SerializedName("HasAssociatedProgressLog") val hasAssociatedProgressLog: Boolean,
    @SerializedName("HasScheduledActivity") val hasScheduledActivity: Boolean,
    @SerializedName("PriorityId") val priorityId: Int
)

fun IncidentReportEntity.toDto() = IncidentReportsDto(
    taskPlanningId = taskPlanningId,
    lostTime = lostTime,
    percentage = percentage,
    quantity = quantity,
    categoryId = categoryId,
    subCategoryId = subCategoryId,
    placeId = placeId,
    levelId = levelId,
    name = name,
    description = description,
    registeredByUserId = registeredByUserId,
    hasAssociatedProgressLog = hasAssociatedProgressLog,
    hasScheduledActivity = hasScheduledActivity,
    priorityId = priorityId
)