package com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lasec.monitoreoapp.data.remote.dto.WorkOrdersDispatchResponse

@Entity(tableName = "work_orders_header")
data class WorkOrderHeaderEntity(
    @PrimaryKey val workOrderId: String,
    val workOrderNumber: String,
    val workShiftId: Int,
    val userId: String?,
    val createdAt: String,
    val phaseId: Int?,
    val moduleId: Int?,
    val inEdition: Boolean?
)

fun WorkOrdersDispatchResponse.toHeaderEntity() = WorkOrderHeaderEntity(
    workOrderId = workOrderId,
    workOrderNumber = workOrderNumber,
    workShiftId = workShiftId,
    userId = userId,
    createdAt = createdAt,
    phaseId = phaseId,
    moduleId = moduleId,
    inEdition = inEdition
)