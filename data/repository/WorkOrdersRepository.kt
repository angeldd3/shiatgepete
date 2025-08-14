package com.lasec.monitoreoapp.data.repository

import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.domain.model.WorkOrder
import javax.inject.Inject

class WorkOrdersRepository @Inject constructor(

)
 {
    suspend fun getWorkOrders(workShiftId: Int, date: String): List<WorkOrder> {
        val response = RetrofitInstance.workOrdersApi.getWorkOrders(workShiftId, date)
        return response
    }
}