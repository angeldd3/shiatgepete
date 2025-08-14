package com.lasec.monitoreoapp.domain.usecase.manual_workorders.create_workorders

import com.lasec.monitoreoapp.data.repository.manual_workorders.AuthorizedVehiclesRepository
import com.lasec.monitoreoapp.data.repository.manual_workorders.ManualWorkOrdersRepository
import com.lasec.monitoreoapp.data.database.dao.manual_workorders.create_workorders.TaskPlanningPlaceLinkDao
import com.lasec.monitoreoapp.data.repository.manual_workorders.create_workorders.WorkOrdersResponseRepository
import javax.inject.Inject

class AuthorizeVehiclesForAssignmentUseCase @Inject constructor(
    private val manualWorkOrdersRepository: ManualWorkOrdersRepository,
    private val workOrdersResponseRepository: WorkOrdersResponseRepository,
    private val authorizedVehiclesRepository: AuthorizedVehiclesRepository
) {
    /**
     * Autoriza TODOS los indexVehicleId presentes en los TaskPlanning del assignment.
     * Lanza error si no hay workOrderId vinculado (asegúrate de haber corrido SaveWorkOrderResponseUseCase antes).
     */
    suspend operator fun invoke(assignmentLocalId: Int) {
        val workOrderId = workOrdersResponseRepository.getWorkOrderIdForAssignment(assignmentLocalId)
            ?: error("No se encontró workOrderId para assignmentLocalId=$assignmentLocalId. ¿Guardaste la respuesta de WorkOrders y los links?")

        val vehicleIds = manualWorkOrdersRepository
            .getPlanningsByAssignmentLocalId(assignmentLocalId)
            .map { it.indexVehicleId }
            .distinct()

        // Lanza si no hay vehículos
        require(vehicleIds.isNotEmpty()) { "No hay indexVehicleId en TaskPlanning para assignmentLocalId=$assignmentLocalId" }

        // POST por cada vehículo (si tu API permite autorizar varios, haz batch; aquí va 1x1)
        for (indexVehicleId in vehicleIds) {
            val resp = authorizedVehiclesRepository.postAuthorizedVehicles(workOrderId, indexVehicleId)
            if (!resp.isSuccessful) {
                val err = resp.errorBody()?.string()
                error("Fallo autorizando vehículo $indexVehicleId para WO $workOrderId. HTTP ${resp.code()} ${err ?: ""}")
            }
        }
    }
}
