package com.lasec.monitoreoapp.data.database.dao.progress_logs_dao

import androidx.room.ColumnInfo
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.ActivityStatusEntity

@Dao
interface ActivityStatusDao {
    @Query("SELECT * FROM activity_status_table WHERE activityStatusId = :id")
    suspend fun getActivityStatusById(id: Int): List<ActivityStatusEntity>

    @Upsert
    suspend fun upsertActivityStatus(activityStatus: List<ActivityStatusEntity>)
}