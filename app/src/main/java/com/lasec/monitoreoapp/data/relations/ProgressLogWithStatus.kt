package com.lasec.monitoreoapp.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.ActivityStatusEntity
import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.ProgressLogEntityFromTasks

data class ProgressLogWithStatus(
    @Embedded val progressLog: ProgressLogEntityFromTasks,

    @Relation(
        parentColumn = "activityStatusId",
        entityColumn = "activityStatusId"
    )
    val activityStatus: ActivityStatusEntity?
)