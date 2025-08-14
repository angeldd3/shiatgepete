package com.lasec.monitoreoapp.data.remote.dto

data class TasksPlanningResponse(
    val taskPlanningId: String,
    val activityTypeId: Int,
    val color: String,
    val endTime: String,
    val indexVehicleId: Int,
    val initTime: String,
    val placeWorkOrderId: String,
    val quantity: Double
)