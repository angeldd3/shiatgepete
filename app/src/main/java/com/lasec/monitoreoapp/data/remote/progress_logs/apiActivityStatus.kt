package com.lasec.monitoreoapp.data.remote.progress_logs

import com.lasec.monitoreoapp.domain.model.progress_logs_model.ActivityStatus
import retrofit2.http.GET

interface ActivityStatusApiService {
    @GET("service/dispatch/api/v1/ProgressLogs/ActivityStatus")
    suspend fun getAllActivityStatus():List<ActivityStatus>
}