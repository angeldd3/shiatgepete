package com.lasec.monitoreoapp.data.remote.manual_workorders

import com.lasec.monitoreoapp.data.remote.dto.TasksPlanningResponse
import com.lasec.monitoreoapp.domain.model.manual_workorders.TasksPlanningDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TasksPlanningApiService {

    @POST("service/dispatch/api/v1/TasksPlanning")
    suspend fun postTasksPlanning(
        @Body tasksPlanning: TasksPlanningDto
    ): Response<TasksPlanningResponse>
}