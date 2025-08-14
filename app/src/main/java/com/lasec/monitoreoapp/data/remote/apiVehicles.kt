package com.lasec.monitoreoapp.data.remote

import com.lasec.monitoreoapp.domain.model.Vehicles
import retrofit2.http.GET


interface VehiclesApiService{
    @GET("service/dispatch/api/v1/DispatchVehicles")
    suspend fun getAllVehicles(): List<Vehicles>
}