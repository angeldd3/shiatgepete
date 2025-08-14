package com.lasec.monitoreoapp.data.remote.manual_workorders

import com.lasec.monitoreoapp.domain.model.manual_workorders.TasksAssignmentDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface TasksAssignmentApiService {

    @POST("service/dispatch/api/v1/TasksAssignment")
    suspend fun postTasksAssignment(
        @Body tasksAssignment: TasksAssignmentDto
    ): Response<Unit>
}