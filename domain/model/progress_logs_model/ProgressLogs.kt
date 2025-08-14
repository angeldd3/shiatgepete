package com.lasec.monitoreoapp.domain.model.progress_logs_model

import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.ProgressLogEntity

data class ProgressLogsDto (
    val quantity: Double,
    val percentage: Double,
    val taskPlanningId: Int,
    val activityStatusId: Int
)

fun ProgressLogEntity.toDto() = ProgressLogsDto(
    quantity = quantity,
    percentage = percentage,
    taskPlanningId = taskPlanningId,
    activityStatusId = activityStatusId
)