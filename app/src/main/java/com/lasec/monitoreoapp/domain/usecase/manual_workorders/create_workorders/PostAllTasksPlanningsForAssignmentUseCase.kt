package com.lasec.monitoreoapp.domain.usecase.manual_workorders.create_workorders

import com.lasec.monitoreoapp.data.remote.dto.TasksPlanningResponse
import com.lasec.monitoreoapp.data.repository.manual_workorders.ManualWorkOrdersRepository
import com.lasec.monitoreoapp.data.repository.manual_workorders.TasksPlanningRepository
import com.lasec.monitoreoapp.data.repository.manual_workorders.create_workorders.WorkOrdersResponseRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class PostAllTasksPlanningsForAssignmentUseCase @Inject constructor(
    private val manualWorkOrdersRepository: ManualWorkOrdersRepository,
    private val workOrdersResponseRepository: WorkOrdersResponseRepository,
    private val tasksPlanningRepo: TasksPlanningRepository
) {
    suspend operator fun invoke(assignmentLocalId: Int): List<TasksPlanningResponse> = withContext(Dispatchers.IO) {
        val plannings = manualWorkOrdersRepository.getPlanningsByAssignmentLocalId(assignmentLocalId)

        val workOrderId = workOrdersResponseRepository
            .getWorkOrderIdForAssignment(assignmentLocalId)
            ?: error("No hay workOrderId vinculado al assignmentLocalId=$assignmentLocalId")

        require(plannings.isNotEmpty()) { "No hay TaskPlanning para el assignmentLocalId=$assignmentLocalId" }

        val results = mutableListOf<TasksPlanningResponse>()

        for (tp in plannings) {
            val placeWorkOrderId = workOrdersResponseRepository.getPlaceWorkOrderId(workOrderId, tp.placeId)
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

            results += response.body()!!
        }

        results
    }
}
