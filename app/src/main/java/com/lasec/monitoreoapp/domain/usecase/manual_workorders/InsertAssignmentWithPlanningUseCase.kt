package com.lasec.monitoreoapp.domain.usecase.manual_workorders


import com.lasec.monitoreoapp.data.database.entities.manual_workorders.TaskAssignmentEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.TaskPlanningEntity
import com.lasec.monitoreoapp.data.repository.manual_workorders.ManualWorkOrdersRepository
import javax.inject.Inject

class InsertAssignmentWithPlanningUseCase @Inject constructor(
    private val manualWorkOrdersRepository: ManualWorkOrdersRepository
) {
    suspend operator fun invoke(
        assignment: TaskAssignmentEntity,
        planningFactory: (Int) -> TaskPlanningEntity
    ) {
        manualWorkOrdersRepository.insertAssignmentWithPlanning(assignment, planningFactory)
    }
}
