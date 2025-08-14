package com.lasec.monitoreoapp.data.database.entities.progress_logs_entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lasec.monitoreoapp.domain.model.progress_logs_model.ActivityStatus


@Entity(tableName = "activity_status_table")
data class ActivityStatusEntity (
    @PrimaryKey
    @ColumnInfo("activityStatusId") val activityStatusId: Int,
    @ColumnInfo("name") val name: String
)


fun ActivityStatus.toDatabase() = ActivityStatusEntity(
    activityStatusId = activityStatusId,
    name = name
)
