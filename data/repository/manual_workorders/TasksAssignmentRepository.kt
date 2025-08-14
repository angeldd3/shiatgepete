package com.lasec.monitoreoapp.data.repository.manual_workorders

import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.domain.model.manual_workorders.TaskPlanningForAssignmentDto
import com.lasec.monitoreoapp.domain.model.manual_workorders.TasksAssignmentDto
import retrofit2.Response
import javax.inject.Inject

class TasksAssignmentRepository @Inject constructor() {

    suspend fun postTasksAssignment(
        indexEmployeeId: Int,
        tasksPlanning: List<TaskPlanningForAssignmentDto>
    ): Response<Unit> {

        // Construir el DTO completo (cloned queda en false por defecto)
        val body = TasksAssignmentDto(
            indexEmployeeId = indexEmployeeId,
            tasksPlanning = tasksPlanning
        )

        // Hacer el POST
        return RetrofitInstance.TasksAssignmentApi.postTasksAssignment(body)
    }
}