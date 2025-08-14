package com.lasec.monitoreoapp.domain.model.manual_workorders

data class WorkOrdersDispatch(
    val workOrderId: String,
    val workOrderNumber: String,
    val placeWorkOrders: List<PlaceWorkOrder>
)

data class PlaceWorkOrder(
    val placeWorkOrderId: String,
    val placeId: Int
)



// Modelo para request de  WorkOrders Dispatch

data class WorkOrdersDispatchDto(
    val levelWorkOrders: List<Any> = emptyList(),
    val mineWorkOrders: List<Any> = emptyList(),
    val minningUnitWorkOrders: List<Any> = emptyList(),
    val moduleId: Int = 2, // constante
    val phaseId: Int = 1, // constante
    val placeWorkOrders: List<PlaceWorkOrderDto>,
    val userId: String = "af6023f5-5b5b-40ce-8e02-c0e3a999becb", // constante
    val workOrderNumber: String = "",
    val workOrderZones: List<Any> = emptyList(),
    val workShiftId: Int
)

data class PlaceWorkOrderDto(
    val placeId: Int,
    val indexRouteId: Int = 0,
    val extractionSequence: Int = 1
)