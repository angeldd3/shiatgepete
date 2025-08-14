package com.lasec.monitoreoapp.domain.model

import com.google.gson.annotations.SerializedName

data class WorkOrder(
    @SerializedName("workOrderId") val workOrderId: Int,
    @SerializedName("workOrderNumber") val workOrderNumber: String,
    @SerializedName("workShiftId") val workShiftId: Int,
    @SerializedName("userId") val userId: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("moduleId") val moduleId: Int,
    @SerializedName("placeWorkOrders") val placeWorkOrders: List<PlaceWorkOrder>


)

data class PlaceWorkOrder(
    @SerializedName("placeWorkOrderId") val placeWorkOrderId: Int,
    @SerializedName("workOrderId") val workOrderId: Int,
    @SerializedName("placeId") val placeId: Int,
    @SerializedName("indexRouteId") val indexRouteId: Int,
    @SerializedName("tonnage") val tonnage: Double,
    @SerializedName("extractionSequence") val extractionSequence: Int,
    @SerializedName("activityTypeId") val activityTypeId: Int,
    @SerializedName("taskPlannings") val taskPlannings: List<TaskPlanning>
)

data class TaskPlanning(
    @SerializedName("taskPlanningId") val taskPlanningId: Int,
    @SerializedName("placeWorkOrderId") val placeWorkOrderId: Int,
    @SerializedName("initTime") val initTime: String,
    @SerializedName("endTime") val endTime: String,
    @SerializedName("indexVehicleId") val indexVehicleId: Int,
    @SerializedName("activityTypeId") val activityTypeId: Int,
    @SerializedName("taskAssignmentId") val taskAssignmentId: Int,
    @SerializedName("quantity") val quantity: Double,
    @SerializedName("processFlowStep") val processFlowStep: Int,
    @SerializedName("taskAssignment") val taskAssignment: TaskAssignment,
    @SerializedName("truckSelectionByTaskPlanning") val truckSelectionByTaskPlanning: TruckSelectionByTaskPlanning,
    @SerializedName("taskPlanningService") val taskPlanningService: Any?, // si puede ser null
    @SerializedName("progressLog") val progressLog: ProgressLog
)

data class TaskAssignment(
    @SerializedName("taskAssignmentId") val taskAssignmentId: Int,
    @SerializedName("indexEmployeeId") val indexEmployeeId: Int,
    @SerializedName("indexEmployee") val indexEmployee: IndexEmployee
)

data class IndexEmployee(
    @SerializedName("indexEmployeeId") val indexEmployeeId: Int,
    @SerializedName("dispatchEmployeeId") val dispatchEmployeeId: Int
)

data class TruckSelectionByTaskPlanning(
    @SerializedName("truckSelectionByTaskPlanningId") val truckSelectionByTaskPlanningId: Int,
    @SerializedName("truckSelectionId") val truckSelectionId: Int,
    @SerializedName("taskPlanningId") val taskPlanningId: Int,
    @SerializedName("initTime") val initTime: String?,
    @SerializedName("endTime") val endTime: String?
)

data class ProgressLog(
    @SerializedName("progressLogId") val progressLogId: Int,
    @SerializedName("taskPlanningId") val taskPlanningId: Int,
    @SerializedName("activityTypeDepartmentOriginWorkOrderId") val activityTypeDepartmentOriginWorkOrderId: Int,
    @SerializedName("activityStatusId") val activityStatusId: Int,
    @SerializedName("percentage") val percentage: Double,
    @SerializedName("quantity") val quantity: Double,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("activityStatus") val activityStatus: Any?
)
