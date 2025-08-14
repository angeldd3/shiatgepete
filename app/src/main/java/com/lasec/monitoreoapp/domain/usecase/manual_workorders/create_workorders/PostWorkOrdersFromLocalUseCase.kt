package com.lasec.monitoreoapp.domain.usecase.manual_workorders.create_workorders

import com.lasec.monitoreoapp.data.remote.dto.WorkOrdersDispatchResponse
import com.lasec.monitoreoapp.data.repository.manual_workorders.ManualWorkOrdersRepository
import com.lasec.monitoreoapp.data.repository.manual_workorders.WorkOrdersDispatchRepository
import javax.inject.Inject


class PostWorkOrdersFromLocalUseCase @Inject constructor(
    private val manualWorkOrdersRepository: ManualWorkOrdersRepository,
    private val workOrdersDispatchRepository: WorkOrdersDispatchRepository
) {
    suspend operator fun invoke(assignmentLocalId: Int): WorkOrdersDispatchResponse {
        val assignment = manualWorkOrdersRepository.getAssignmentByLocalId(assignmentLocalId)
            ?: error("Assignment no encontrado")

        val plannings = manualWorkOrdersRepository.getPlanningsByAssignmentLocalId(assignmentLocalId)
        require(plannings.isNotEmpty()) { "No hay plannings para el assignment" }

        // Distintos lugares ordenados por hora de inicio (siempre que initTime sea ISO)
        val placeIdsOrdered = plannings
            .groupBy { it.placeId }
            .map { (placeId, items) -> placeId to (items.minOfOrNull { it.initTime } ?: "") }
            .sortedBy { it.second }
            .map { it.first }

        val resp = workOrdersDispatchRepository.postWorkOrdersDispatch(
            placeIds = placeIdsOrdered,
            workShiftId = assignment.workShiftId
        )

        if (!resp.isSuccessful || resp.body() == null) {
            error("HTTP ${resp.code()} ${resp.errorBody()?.string()}")
        }
        return resp.body()!!
    }
}
