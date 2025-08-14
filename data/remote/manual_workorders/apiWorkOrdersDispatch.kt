package com.lasec.monitoreoapp.data.remote.manual_workorders

import com.lasec.monitoreoapp.data.remote.dto.WorkOrdersDispatchResponse
import com.lasec.monitoreoapp.domain.model.manual_workorders.WorkOrdersDispatch
import com.lasec.monitoreoapp.domain.model.manual_workorders.WorkOrdersDispatchDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface WorkOrdersDispatchApiService {

    @GET("service/dispatch/api/v1/WorkOrders")
    suspend fun getWorkOrdersDispatch(): List<WorkOrdersDispatch>

    @POST("service/dispatch/api/v1/WorkOrders")
    suspend fun postWorkOrdersDispatch(
        @Body workOrders: WorkOrdersDispatchDto
    ): Response<WorkOrdersDispatchResponse>

}