package com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders

import androidx.room.Entity

@Entity(
    tableName = "task_planning_remote_map",
    primaryKeys = ["taskPlanningLocalId"]
)
data class TaskPlanningRemoteMapEntity(
    val taskPlanningLocalId: Int,
    val taskPlanningId: String,
    val activityTypeId: Int,
    val color: String,
    val endTime: String,
    val indexVehicleId: Int,
    val initTime: String,
    val placeWorkOrderId: String,
    val quantity: Double
)
