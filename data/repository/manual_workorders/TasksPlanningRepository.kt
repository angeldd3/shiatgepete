package com.lasec.monitoreoapp.data.repository.manual_workorders

import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.data.remote.dto.TasksPlanningResponse
import com.lasec.monitoreoapp.domain.model.manual_workorders.TasksPlanningDto
import retrofit2.Response
import javax.inject.Inject

class TasksPlanningRepository @Inject constructor() {

    suspend fun postTasksPlanning(
        activityTypeId: Int,
        endTime: String,
        indexVehicleId: Int,
        initTime: String,
        placeWorkOrderId: String,
        quantity: Double,
        vehicleTypeId: Int
    ): Response<TasksPlanningResponse> {

        // Construir el DTO con valores por defecto en los demás campos
        val body = TasksPlanningDto(
            activityTypeId = activityTypeId,
            endTime = endTime,
            indexVehicleId = indexVehicleId,
            initTime = initTime,
            placeWorkOrderId = placeWorkOrderId,
            quantity = quantity,
            vehicleTypeId = vehicleTypeId
        )

        // Hacer el POST
        return RetrofitInstance.tasksPlanningApi.postTasksPlanning(body)
    }

}