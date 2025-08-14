package com.lasec.monitoreoapp.data.repository.progress_logs_repository

import com.lasec.monitoreoapp.data.database.dao.progress_logs_dao.ProgressLogDaoFromTasks
import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.ProgressLogEntityFromTasks
import javax.inject.Inject

class ProgressLogRepositoryFromTasks @Inject constructor(private val progressLogDaoFromTasks: ProgressLogDaoFromTasks) {

    suspend fun upsertProgressLogFromTasksToDatabase(progressLogEntityFromTasks: ProgressLogEntityFromTasks) {
        progressLogDaoFromTasks.upsertProgressLogFromTasks(progressLogEntityFromTasks)
    }
}