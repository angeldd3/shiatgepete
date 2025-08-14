package com.lasec.monitoreoapp.domain.usecase.manual_workorders.create_workorders


import com.lasec.monitoreoapp.data.repository.manual_workorders.AuthorizedEmployeesRepository
import com.lasec.monitoreoapp.data.repository.manual_workorders.ManualWorkOrdersRepository
import com.lasec.monitoreoapp.data.repository.manual_workorders.create_workorders.WorkOrdersResponseRepository
import javax.inject.Inject

class AuthorizedEmployeesForAssignmentUseCase @Inject constructor(
    private val manualWorkOrdersRepository: ManualWorkOrdersRepository,
    private val workOrdersResponseRepository: WorkOrdersResponseRepository,
    private val authorizedEmployeesRepository: AuthorizedEmployeesRepository
) {
    /**
     * Autoriza el indexEmployeeId del TaskAssignment.
     * Lanza error si no hay workOrderId vinculado (asegúrate de haber corrido SaveWorkOrderResponseUseCase antes).
     */
    suspend operator fun invoke(assignmentLocalId: Int) {
        val workOrderId = workOrdersResponseRepository.getWorkOrderIdForAssignment(assignmentLocalId)
            ?: error("No se encontró workOrderId para assignmentLocalId=$assignmentLocalId. ¿Guardaste la respuesta de WorkOrders y los links?")

        val assignment = manualWorkOrdersRepository.getAssignmentByLocalId(assignmentLocalId)
        val indexEmployeeId = assignment.indexEmployeeId

        val resp = authorizedEmployeesRepository.postAuthorizedEmployees(workOrderId, indexEmployeeId)
        if (!resp.isSuccessful) {
            val err = resp.errorBody()?.string()
            error("Fallo autorizando empleado $indexEmployeeId para WO $workOrderId. HTTP ${resp.code()} ${err ?: ""}")

        }
    }
}