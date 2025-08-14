package com.lasec.monitoreoapp.domain.model


import com.lasec.monitoreoapp.data.database.entities.ShiftEntity
import com.lasec.monitoreoapp.data.relations.TaskWithRelations

data class TaskFullDetail(
    val taskPlanningId: Int,
    val quantity: Float,
    val initTime: String,
    val endTime: String,

    // Activity
    val activityId: Int,
    val activityName: String,


    // Place
    val placeId: Int,
    val placeName: String,
    val levelId: Int?,

    // Vehicle
    val vehicleId: Int,
    val economicNumber: String,
    val vehicleTypeId: Int,

    // Order
    val orderId: Int,
    val workOrderNumber: String,
    val createdAt: String,

    // Shift
    val shiftId: Int,
    val shiftDescription: String,
    val shiftStartTime: String,
    val shiftEndTime: String,

    //Employee
    val dispatchEmployeeId: Int,
    val indexEmployeeId: Int,

    // ProgressLog
    val progressLogId: Int?,
    val activityStatusId: Int?,
    val activityStatusName: String?,
    val progressPercentage: Double?,
    val progressQuantity: Double?,
    val progressCreatedAt: String?


)


fun TaskWithRelations.toDomain(shift: ShiftEntity): TaskFullDetail {
    return TaskFullDetail(
        taskPlanningId = task.taskPlanningId,
        quantity = task.quantity,
        initTime = task.initTime,
        endTime = task.endTime,

        activityId = activity.activityTypeId,
        activityName = activity.name,


        placeId = place.id,
        placeName = place.name,
        levelId = place.levelId,

        vehicleId = vehicle.indexVehicleId,
        economicNumber = vehicle.economicNumber,
        vehicleTypeId = vehicle.vehicleTypeId,

        orderId = order.id,
        workOrderNumber = order.workOrderNumber,
        createdAt = order.createdAt,

        shiftId = shift.id,
        shiftDescription = shift.description,
        shiftStartTime = shift.startTime,
        shiftEndTime = shift.endTime,

        dispatchEmployeeId = employee.DispatchEmployeeId,
        indexEmployeeId = employee.IndexEmployeeId,

        progressLogId = progressLogWithStatus?.progressLog?.progressLogId,
        activityStatusId = progressLogWithStatus?.progressLog?.activityStatusId,
        activityStatusName = progressLogWithStatus?.activityStatus?.name,
        progressPercentage = progressLogWithStatus?.progressLog?.percentage,
        progressQuantity = progressLogWithStatus?.progressLog?.quantity,
        progressCreatedAt = progressLogWithStatus?.progressLog?.createdAt
    )
}






