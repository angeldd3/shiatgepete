package com.lasec.monitoreoapp.data.remote


import com.lasec.monitoreoapp.domain.model.WorkOrder
import retrofit2.http.GET
import retrofit2.http.Path

interface WorkOrdersApiService {
    @GET("service/dispatch/api/v1/Monitor/{workShiftId}/{createdAt}")
    suspend fun getWorkOrders(
        @Path("workShiftId") workShiftId: Int,
        @Path("createdAt") createdAt: String // formato: yyyy-MM-dd
    ): List<WorkOrder>


}



