package com.lasec.monitoreoapp.domain.usecase.manual_workorders.create_workorders


import com.lasec.monitoreoapp.data.database.dao.manual_workorders.create_workorders.TaskPlanningRemoteMapDao
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders.TaskPlanningRemoteMapEntity
import com.lasec.monitoreoapp.data.remote.dto.TasksPlanningResponse
import com.lasec.monitoreoapp.data.repository.manual_workorders.ManualWorkOrdersRepository
import javax.inject.Inject

/**
 * Persiste las respuestas de TaskPlanning vinculado cada planificación local con los datos remotos
 * devueltos por el backend.
 */
class SaveTpResponseUseCase @Inject constructor(
    private val manualWorkOrdersRepository: ManualWorkOrdersRepository,
    private val taskPlanningRemoteMapDao: TaskPlanningRemoteMapDao
) {
    suspend operator fun invoke(
        assignmentLocalId: Int,
        responses: List<TasksPlanningResponse>
    ) {
        val plannings = manualWorkOrdersRepository.getPlanningsByAssignmentLocalId(assignmentLocalId)
        if (plannings.isEmpty() || responses.isEmpty()) return

        require(plannings.size == responses.size) {
            "El número de respuestas (${responses.size}) no coincide con la cantidad de plannings (${plannings.size})"
        }

        for ((planning, resp) in plannings.zip(responses)) {
            taskPlanningRemoteMapDao.insertMap(
                TaskPlanningRemoteMapEntity(
                    taskPlanningLocalId = planning.taskPlanningLocalId,
                    taskPlanningId = resp.taskPlanningId,
                    activityTypeId = resp.activityTypeId,
                    color = resp.color,
                    endTime = resp.endTime,
                    indexVehicleId = resp.indexVehicleId,
                    initTime = resp.initTime,
                    placeWorkOrderId = resp.placeWorkOrderId,
                    quantity = resp.quantity
                )
            )
        }
    }
}