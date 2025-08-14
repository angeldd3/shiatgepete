package com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.TaskPlanningEntity

@Entity(
    tableName = "task_planning_place_link",
    indices = [Index(value = ["placeWorkOrderId"]), Index(value = ["placeId"])],
    foreignKeys = [
        ForeignKey(
            entity = TaskPlanningEntity::class,
            parentColumns = ["taskPlanningLocalId"],
            childColumns = ["taskPlanningLocalId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlaceWorkOrderEntity::class,
            parentColumns = ["placeWorkOrderId"],
            childColumns = ["placeWorkOrderId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class TaskPlanningPlaceLinkEntity(
    @PrimaryKey val taskPlanningLocalId: Int,
    val placeWorkOrderId: String,
    val placeId: Int,          // redundante pero útil para queries rápidas por lugar
    val workOrderId: String    // redundante; te ahorra un join si lo necesitas
)