package com.lasec.monitoreoapp.domain.usecase.manual_workorders.create_workorders

import com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders.PlaceWorkOrderEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders.TaskPlanningPlaceLinkEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders.toHeaderEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders.toPlaceEntities
import com.lasec.monitoreoapp.data.remote.dto.WorkOrdersDispatchResponse
import com.lasec.monitoreoapp.data.repository.manual_workorders.ManualWorkOrdersRepository
import com.lasec.monitoreoapp.data.repository.manual_workorders.create_workorders.WorkOrdersResponseRepository
import javax.inject.Inject




class SaveWorkOrderResponseUseCase @Inject constructor(
    private val manualWorkOrdersRepository: ManualWorkOrdersRepository,              // Lee TaskPlanning por assignmentLocalId
    private val workOrdersResponseRepository: WorkOrdersResponseRepository         // Inserta header, places y links
) {
    /**
     * Persiste la respuesta de WorkOrders y vincula TaskPlanning -> PlaceWorkOrder.
     *
     * Flujo:
     * 1) Guarda WorkOrderHeader
     * 2) Guarda PlaceWorkOrders
     * 3) Genera links TaskPlanningLocalId ↔ placeWorkOrderId por placeId
     */
    suspend operator fun invoke(
        assignmentLocalId: Int,
        response: WorkOrdersDispatchResponse
    ) {
        // 1) Header
        workOrdersResponseRepository.insertWorkOrderHeaderToDatabase(response.toHeaderEntity())

        // 2) Places
        val placeEntities: List<PlaceWorkOrderEntity> = response.toPlaceEntities()
        if (placeEntities.isNotEmpty()) {
            workOrdersResponseRepository.insertPlaceWorkOrderToDatabase(placeEntities)
        } else {
            // Sin places no hay nada que vincular
            return
        }

        // 3) Links: TaskPlanning ↔ PlaceWorkOrder por placeId
        val plannings = manualWorkOrdersRepository.getPlanningsByAssignmentLocalId(assignmentLocalId)
        if (plannings.isEmpty()) return

        // Si hay múltiples placeWorkOrders para un mismo placeId, elige el de menor extractionSequence
        val bestByPlaceId: Map<Int, PlaceWorkOrderEntity> = placeEntities
            .groupBy { it.placeId }
            .mapValues { (_, list) -> list.minByOrNull { it.extractionSequence } ?: list.first() }

        val links: List<TaskPlanningPlaceLinkEntity> = plannings.mapNotNull { tp ->
            val match = bestByPlaceId[tp.placeId] ?: return@mapNotNull null
            TaskPlanningPlaceLinkEntity(
                taskPlanningLocalId = tp.taskPlanningLocalId,
                placeWorkOrderId = match.placeWorkOrderId,
                placeId = tp.placeId,
                workOrderId = response.workOrderId
            )
        }

        if (links.isNotEmpty()) {
            workOrdersResponseRepository.insertTaskPlanningPlaceLinkToDatabase(links)
        }
    }
}

