package com.lasec.monitoreoapp.domain.usecase.manual_workorders.create_workorders

import android.os.Build
import androidx.annotation.RequiresApi
import com.lasec.monitoreoapp.data.remote.dto.WorkOrdersDispatchResponse
import com.lasec.monitoreoapp.domain.model.manual_workorders.RegisterActivityParams
import javax.inject.Inject

class RegisterAndPostWorkOrderUseCase @Inject constructor(
    private val registerLocal: RegisterActivityLocalUseCase,
    private val postFromLocal: PostWorkOrdersFromLocalUseCase,
    private val saveWoResponse: SaveWorkOrderResponseUseCase,
    private val authorizeVehiclesForAssignment: AuthorizeVehiclesForAssignmentUseCase,
    private val postAllTasksPlannings: PostAllTasksPlanningsForAssignmentUseCase

) {
    /**
     * Flujo:
     * 1) Guarda TaskAssignment + TaskPlanning (UTC Z) → assignmentLocalId
     * 2) POST WorkOrders (usa assignmentLocalId para armar placeIds ordenados)
     * 3) Guarda header/places y crea links (TaskPlanning ↔ placeWorkOrderId)
     */
    @RequiresApi(Build.VERSION_CODES.O)
    suspend operator fun invoke(p: RegisterActivityParams): WorkOrdersDispatchResponse {
        // 1) Local
        val assignmentLocalId = registerLocal(p)

        // 2) POST
        val response = postFromLocal(assignmentLocalId)

        // 3) Persistir respuesta y vincular con los plannings locales
        saveWoResponse(
            assignmentLocalId = assignmentLocalId,
            response = response
        )

        authorizeVehiclesForAssignment(assignmentLocalId)
        postAllTasksPlannings(assignmentLocalId)


        return response
    }
}
