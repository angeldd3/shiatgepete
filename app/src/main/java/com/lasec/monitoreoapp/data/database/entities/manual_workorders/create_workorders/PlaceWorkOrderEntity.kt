package com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lasec.monitoreoapp.data.remote.dto.WorkOrdersDispatchResponse

@Entity(
    tableName = "place_work_orders",
    indices = [Index(value = ["workOrderId"]), Index(value = ["placeId"])],
    foreignKeys = [
        ForeignKey(
            entity = WorkOrderHeaderEntity::class,
            parentColumns = ["workOrderId"],
            childColumns = ["workOrderId"],
            onDelete = ForeignKey.CASCADE
        )
    ]
)
data class PlaceWorkOrderEntity(
    @PrimaryKey val placeWorkOrderId: String,
    val workOrderId: String,
    val placeId: Int,
    val indexRouteId: Int,
    val extractionSequence: Int,
    val tonnage: Double,
    val activityTypeId: Int
)

fun WorkOrdersDispatchResponse.toPlaceEntities(): List<PlaceWorkOrderEntity> =
    placeWorkOrders.map {
        PlaceWorkOrderEntity(
            placeWorkOrderId = it.placeWorkOrderId,
            workOrderId = it.workOrderId,
            placeId = it.placeId,
            indexRouteId = it.indexRouteId,
            extractionSequence = it.extractionSequence,
            tonnage = it.tonnage,
            activityTypeId = it.activityTypeId
        )
    }