package com.lasec.monitoreoapp.data.repository

import com.lasec.monitoreoapp.data.database.dao.TasksDao
import com.lasec.monitoreoapp.data.database.entities.TasksEntity
import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.domain.model.TaskFullDetail
import com.lasec.monitoreoapp.domain.model.toDomain
import javax.inject.Inject

class TasksRepository @Inject constructor(private val taskDao: TasksDao, private val workShiftsRepository: WorkShiftsRepository) {

    suspend fun upsert(task: TasksEntity){
        return taskDao.upsertTasks(task)
    }

    suspend fun getTaskWithRelations() = taskDao.getTasksWithDetails()

    suspend fun getTaskWithRelationsByEmployeeId(employeeId: Int) = taskDao.getTasksWithDetailsByEmployeeId(employeeId)


}