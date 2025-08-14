package com.lasec.monitoreoapp.domain.usecase

import android.util.Log
import com.lasec.monitoreoapp.data.repository.WorkOrdersRepository
import com.lasec.monitoreoapp.domain.model.WorkOrder
import java.time.LocalDate
import javax.inject.Inject

class GetWorkOrdersForEmployeeUseCase @Inject constructor(
    private val workShiftsUseCase: GetWorkshiftsUseCase,
    private val workOrdersRepository: WorkOrdersRepository
) {
    suspend operator fun invoke(dispatchEmployeeId: Int): List<WorkOrder> {
        val turnoActual = workShiftsUseCase.getTurnoActual() ?: return emptyList()
        val today = LocalDate.now()
        val createdAt = "${today.year}-${today.monthValue}-${today.dayOfMonth}"

        val ordenes = workOrdersRepository.getWorkOrders(turnoActual.id, createdAt)
//        val ordenes = workOrdersRepository.getWorkOrders(1, "2025-7-10")
        Log.d("USE_CASE", "Órdenes crudas desde API: ${ordenes.size}")

        return ordenes.map { orden ->
            orden.copy(
                placeWorkOrders = orden.placeWorkOrders.map { pwo ->
                    pwo.copy(
                        taskPlannings = pwo.taskPlannings.filter {
                            it.taskAssignment.indexEmployee.dispatchEmployeeId == dispatchEmployeeId
                        }
                    )
                }.filter { it.taskPlannings.isNotEmpty() }
            )
        }.filter { it.placeWorkOrders.isNotEmpty() }
    }
}
