package com.lasec.monitoreoapp.data.database.dao.progress_logs_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.ProgressLogEntity

@Dao
interface ProgressLogsDao {
    @Insert
    suspend fun insertProgressLogs(entity: ProgressLogEntity)

    @Query("SELECT * FROM progress_logs_table ORDER BY id ASC")
    suspend fun getAllProgressLogs(): List<ProgressLogEntity>

    @Query("DELETE FROM progress_logs_table WHERE id = :id")
    suspend fun deleteById(id: Int)
}
