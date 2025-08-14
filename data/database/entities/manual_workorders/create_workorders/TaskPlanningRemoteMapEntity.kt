package com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders

import androidx.room.Entity

@Entity(
    tableName = "task_planning_remote_map",
    primaryKeys = ["taskPlanningLocalId"]
)
data class TaskPlanningRemoteMapEntity(
    val taskPlanningLocalId: Int,
    val taskPlanningIdRemote: String
)
