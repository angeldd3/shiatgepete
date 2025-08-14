package com.lasec.monitoreoapp.data.remote

import com.lasec.monitoreoapp.domain.model.Empleado

import retrofit2.http.GET

interface EmployeeApiService {
    @GET("service/dispatch/api/v1/DispatchEmployees")
    suspend fun getEmployees(): List<Empleado>
}
