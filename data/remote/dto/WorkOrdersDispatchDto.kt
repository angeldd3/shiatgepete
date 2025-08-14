// data/remote/dto/WorkOrdersDispatchResponse.kt
package com.lasec.monitoreoapp.data.remote.dto

data class WorkOrdersDispatchResponse(
    val workOrderId: String,
    val workOrderNumber: String,
    val workShiftId: Int,
    val userId: String?,
    val createdAt: String,     // ISO del backend
    val phaseId: Int?,
    val moduleId: Int?,
    val inEdition: Boolean?,
    val placeWorkOrders: List<PlaceWorkOrderResponse> = emptyList()
)

data class PlaceWorkOrderResponse(
    val placeWorkOrderId: String,
    val workOrderId: String,
    val placeId: Int,
    val indexRouteId: Int,
    val extractionSequence: Int,
    val tonnage: Double,
    val activityTypeId: Int
)


