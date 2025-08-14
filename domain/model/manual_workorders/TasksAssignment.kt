package com.lasec.monitoreoapp.domain.model.manual_workorders


data class TasksAssignmentDto(
    val indexEmployeeId: Int,
    val tasksPlanning: List<TaskPlanningForAssignmentDto>,
    val cloned: Boolean = false
)

data class TaskPlanningForAssignmentDto(
    val taskPlanningId: String,
    val activityTypeId: Int,
    val color: String,
    val endTime: String,
    val indexVehicleId: Int,
    val initTime: String,
    val placeWorkOrderId: String,
    val quantity: Double,
    val workOrderNumberEmployee: String
)
