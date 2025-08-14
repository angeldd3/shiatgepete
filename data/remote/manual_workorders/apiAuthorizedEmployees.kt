package com.lasec.monitoreoapp.data.remote.manual_workorders

import com.lasec.monitoreoapp.domain.model.manual_workorders.AuthorizedEmployeesDto
import retrofit2.Response

import retrofit2.http.Body
import retrofit2.http.POST

interface AuthorizedEmployeesApiService {

    @POST("service/dispatch/api/v1/AuthorizedEmployees")
    suspend fun postAuthorizedEmployees(
        @Body authorizedEmployees: AuthorizedEmployeesDto
    ): Response<Unit>
}