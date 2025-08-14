package com.lasec.monitoreoapp.data.remote.dto

data class ActivityStatus(
    val activityStatusId: Int,
    val name: String
)

data class ProgressLogResponse(
    val progressLogId: Int,
    val taskPlanningId: Int,
    val activityTypeDepartmentOriginWorkOrderId: Int,
    val activityStatusId: Int,
    val percentage: Double,
    val quantity: Double,
    val createdAt: String,
    val active: Boolean,
    val activityStatus: ActivityStatus
)
