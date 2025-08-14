package com.lasec.monitoreoapp.domain.usecase.tasks

import android.util.Log
import com.lasec.monitoreoapp.data.repository.TasksRepository
import com.lasec.monitoreoapp.data.repository.WorkShiftsRepository
import com.lasec.monitoreoapp.domain.model.TaskFullDetail
import com.lasec.monitoreoapp.domain.model.toDomain
import javax.inject.Inject

class GetTaskWithDetailsUseCase @Inject constructor(
    private val tasksRepository: TasksRepository,
    private val workShiftsRepository: WorkShiftsRepository
) {
    suspend operator fun invoke(employeeId: Int): List<TaskFullDetail> {
        val taskWithRelations = tasksRepository.getTaskWithRelationsByEmployeeId(employeeId)
        return taskWithRelations.map { relation ->
            val shift = workShiftsRepository.getById(relation.order.shiftId)
            relation.toDomain(shift)
        }
    }
}



