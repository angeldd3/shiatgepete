package com.lasec.monitoreoapp.data.remote

import com.lasec.monitoreoapp.domain.model.WorkShifts
import retrofit2.http.GET

interface WorkShiftApiService {
    @GET("service/catalog/api/v1/workshifts/all")
    suspend fun getWorkShifts(): List<WorkShifts>
}

