package com.lasec.monitoreoapp.data.database.dao.progress_logs_dao

import androidx.room.Dao
import androidx.room.Upsert
import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.ProgressLogEntityFromTasks


@Dao
interface ProgressLogDaoFromTasks {
    @Upsert
    suspend fun upsertProgressLogFromTasks(progressLogFromTasks: ProgressLogEntityFromTasks)
}