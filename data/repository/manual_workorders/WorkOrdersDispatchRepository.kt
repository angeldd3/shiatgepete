package com.lasec.monitoreoapp.data.repository.manual_workorders

import com.lasec.monitoreoapp.data.database.entities.manual_workorders.AuthorizedPlaceEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.toDatabase
import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.data.remote.dto.WorkOrdersDispatchResponse
import com.lasec.monitoreoapp.domain.model.manual_workorders.PlaceWorkOrderDto
import com.lasec.monitoreoapp.domain.model.manual_workorders.WorkOrdersDispatchDto
import retrofit2.Response
import javax.inject.Inject

class WorkOrdersDispatchRepository @Inject constructor() {

    suspend fun getAllWorkOrdersDispatchFromApi(): List<AuthorizedPlaceEntity> {
        val response = RetrofitInstance.workOrdersDispatchApi.getWorkOrdersDispatch()
        return response.flatMap { workOrder ->
            workOrder.placeWorkOrders.map { it.toDatabase() }
        }
    }


    suspend fun postWorkOrdersDispatch(
        placeIds: List<Int>,
        workShiftId: Int
    ): Response<WorkOrdersDispatchResponse> {

        // Armar la lista de placeWorkOrders
        val placeWorkOrdersList = placeIds.map { placeId ->
            PlaceWorkOrderDto(placeId = placeId)
        }

        // Construir el DTO completo
        val body = WorkOrdersDispatchDto(
            placeWorkOrders = placeWorkOrdersList,
            workShiftId = workShiftId
        )

        // Hacer el POST
        return RetrofitInstance.workOrdersDispatchApi.postWorkOrdersDispatch(body)
    }
}