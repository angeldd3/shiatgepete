package com.lasec.monitoreoapp.data.remote.progress_logs

import com.lasec.monitoreoapp.data.remote.dto.ProgressLogResponse
import com.lasec.monitoreoapp.domain.model.progress_logs_model.ProgressLogsDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ProgressLogsApiService{
    @POST("service/dispatch/api/v1/ProgressLogs")
    suspend fun postProgressLogs(
        @Body progressLog: ProgressLogsDto
    ): Response<ProgressLogResponse> // Puedes cambiar Unit por un modelo de respuesta si el endpoint devuelve algo
}