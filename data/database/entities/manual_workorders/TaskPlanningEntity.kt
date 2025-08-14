package com.lasec.monitoreoapp.data.database.entities.manual_workorders

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "task_planning_table",
    foreignKeys = [ForeignKey(
        entity = TaskAssignmentEntity::class,
        parentColumns = ["assignmentLocalId"],
        childColumns = ["assignmentLocalId"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["assignmentLocalId"])]

)
data class TaskPlanningEntity(
    @PrimaryKey(autoGenerate = true) val taskPlanningLocalId: Int = 0,

    val assignmentLocalId: Int,

    val indexVehicleId: Int,
    val economicNumber: String,
    val vehicleTypeId: Int,

    val activityTypeId: Int,
    val activityName: String,

    val quantity: Double,

    val placeId: Int,
    val placeName: String,

    val initTime: String,  // ISO 8601
    val endTime: String
)