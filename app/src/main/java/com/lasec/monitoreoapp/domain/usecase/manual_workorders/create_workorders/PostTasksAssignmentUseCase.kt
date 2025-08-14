package com.lasec.monitoreoapp.domain.usecase.manual_workorders.create_workorders

import android.os.Build
import androidx.annotation.RequiresApi
import com.lasec.monitoreoapp.data.database.dao.manual_workorders.create_workorders.TaskPlanningRemoteMapDao
import com.lasec.monitoreoapp.data.repository.manual_workorders.ManualWorkOrdersRepository
import com.lasec.monitoreoapp.data.repository.manual_workorders.TasksAssignmentRepository
import com.lasec.monitoreoapp.data.repository.manual_workorders.create_workorders.WorkOrdersResponseRepository
import com.lasec.monitoreoapp.domain.model.manual_workorders.TaskPlanningForAssignmentDto
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class PostTasksAssignmentUseCase @Inject constructor(
    private val manualWorkOrdersRepository: ManualWorkOrdersRepository,
    private val workOrdersResponseRepository: WorkOrdersResponseRepository,
    private val taskPlanningRemoteMapDao: TaskPlanningRemoteMapDao,
    private val tasksAssignmentRepository: TasksAssignmentRepository,
) {

    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(assignmentLocalId: Int) = withContext(Dispatchers.IO) {
        val assignment = manualWorkOrdersRepository.getAssignmentByLocalId(assignmentLocalId)
        val plannings = manualWorkOrdersRepository.getPlanningsByAssignmentLocalId(assignmentLocalId)

        require(plannings.isNotEmpty()) { "No hay TaskPlanning para el assignmentLocalId=$assignmentLocalId" }

        val workOrderId = workOrdersResponseRepository
            .getWorkOrderIdForAssignment(assignmentLocalId)
            ?: error("No hay workOrderId vinculado al assignmentLocalId=$assignmentLocalId")

        val header = workOrdersResponseRepository
            .getWorkOrderHeader(workOrderId)
            ?: error("No se encontró WorkOrderHeader para workOrderId=$workOrderId")

        val date = LocalDate.parse(header.createdAt.substring(0, 10))
        val formattedDate = date.format(DateTimeFormatter.ofPattern("ddMMyyyy"))
        val workOrderNumberEmployee =
            "${header.workOrderNumber}-$formattedDate-$workOrderId-${assignment.indexEmployeeId}"

        val planningDtos = plannings.map { tp ->
            val remote = taskPlanningRemoteMapDao.getByLocalId(tp.taskPlanningLocalId)
                ?: error("No se encontró mapeo remoto para taskPlanningLocalId=${tp.taskPlanningLocalId}")
            TaskPlanningForAssignmentDto(
                taskPlanningId = remote.taskPlanningId,
                activityTypeId = remote.activityTypeId,
                color = remote.color,
                endTime = remote.endTime,
                indexVehicleId = remote.indexVehicleId,
                initTime = remote.initTime,
                placeWorkOrderId = remote.placeWorkOrderId,
                quantity = remote.quantity,
                workOrderNumberEmployee = workOrderNumberEmployee
            )
        }

        val response = tasksAssignmentRepository.postTasksAssignment(
            indexEmployeeId = assignment.indexEmployeeId,
            tasksPlanning = planningDtos
        )

        if (!response.isSuccessful) {
            error("Error al postear TasksAssignment: HTTP ${response.code()} - ${response.errorBody()?.string()}")
        }
    }
}