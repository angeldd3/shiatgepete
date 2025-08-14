package com.lasec.monitoreoapp.domain.model.manual_workorders

data class TasksPlanningDto(
    val activityTypeId: Int,
    val endTime: String,
    val indexVehicleId: Int,
    val initTime: String,
    val placeWorkOrderId: String,
    val quantity: Double,
    val vehicleTypeId: Int,

    // Campos con valores por defecto
    val color: String = "#295F98",
    val processFlowStep: Int = 1,
    val taskAssignmentId: String? = null
)