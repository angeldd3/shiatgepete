package com.lasec.monitoreoapp.domain.usecase.manual_workorders.create_workorders

import com.lasec.monitoreoapp.data.database.dao.manual_workorders.create_workorders.TaskPlanningRemoteMapDao
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders.TaskPlanningRemoteMapEntity
import com.lasec.monitoreoapp.data.repository.manual_workorders.ManualWorkOrdersRepository
import com.lasec.monitoreoapp.data.repository.manual_workorders.TasksPlanningRepository
import com.lasec.monitoreoapp.data.repository.manual_workorders.create_workorders.WorkOrdersResponseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostAllTasksPlanningsForAssignmentUseCase @Inject constructor(
    private val manualWorkOrdersRepository: ManualWorkOrdersRepository,
    private val workOrdersResponseRepository: WorkOrdersResponseRepository,
    private val tasksPlanningRepo: TasksPlanningRepository,
    private val taskPlanningRemoteMapDao: TaskPlanningRemoteMapDao
) {
    suspend operator fun invoke(assignmentLocalId: Int) = withContext(Dispatchers.IO) {
        // 1) Obtener todos los TaskPlanning locales para este assignment
        val plannings = manualWorkOrdersRepository.getPlanningsByAssignmentLocalId(assignmentLocalId)
        val workOrderId = workOrdersResponseRepository.getWorkOrderIdForAssignment(assignmentLocalId)

        require(plannings.isNotEmpty()) {
            "No hay TaskPlanning para el assignmentLocalId=$assignmentLocalId"
        }

        // 2) Iterar y postear cada uno
        for (tp in plannings) {
            // Buscar el placeWorkOrderId correspondiente al placeId de este planning
            val placeWorkOrderId = workOrdersResponseRepository.getPlaceWorkOrderId(workOrderId,tp.placeId)
                ?: error("No se encontró placeWorkOrderId para placeId=${tp.placeId}")

            val response = tasksPlanningRepo.postTasksPlanning(
                activityTypeId = tp.activityTypeId,
                endTime = tp.endTime,
                indexVehicleId = tp.indexVehicleId,
                initTime = tp.initTime,
                placeWorkOrderId = placeWorkOrderId,
                quantity = tp.quantity,
                vehicleTypeId = tp.vehicleTypeId
            )

            if (!response.isSuccessful || response.body() == null) {
                error("Error al postear TaskPlanning: HTTP ${response.code()} - ${response.errorBody()?.string()}")
            }

            val remoteId = response.body()!!.taskPlanningId

            // 3) Guardar mapeo local → remoto en Room
            taskPlanningRemoteMapDao.insertMap(
                TaskPlanningRemoteMapEntity(
                    taskPlanningLocalId = tp.taskPlanningLocalId,
                    taskPlanningIdRemote = remoteId
                )
            )
        }
    }
}
