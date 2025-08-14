package com.lasec.monitoreoapp.data.repository.manual_workorders

import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.domain.model.manual_workorders.AuthorizedEmployeesDto
import retrofit2.Response
import javax.inject.Inject

class AuthorizedEmployeesRepository @Inject constructor() {

    suspend fun postAuthorizedEmployees(
        workOrderId: String,
        indexEmployeeId: Int
    ): Response<Unit> {
        val body = AuthorizedEmployeesDto(
            workOrderId = workOrderId,
            indexEmployeeId = indexEmployeeId
        )
        return RetrofitInstance.AuthorizedEmployeesApi.postAuthorizedEmployees(body)
    }
}