package com.lasec.monitoreoapp.data.remote.manual_workorders

import com.lasec.monitoreoapp.domain.model.manual_workorders.DispatchVehicleTypes
import retrofit2.http.GET

interface DispatchVehicleTypeApiService {
    @GET("service/dispatch/api/v1/DispatchVehicleTypes")
    suspend fun getAllDispatchVehicleTypes(): List<DispatchVehicleTypes>
}