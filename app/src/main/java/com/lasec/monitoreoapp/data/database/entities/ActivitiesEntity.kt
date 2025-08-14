package com.lasec.monitoreoapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lasec.monitoreoapp.domain.model.Activities

@Entity(tableName = "activities_table")
data class ActivitiesEntity(
    @PrimaryKey
    @ColumnInfo(name = "activityTypeId") val activityTypeId: Int,
    @ColumnInfo(name = "name") val name: String,

)


fun Activities.toDatabase() = ActivitiesEntity(
    activityTypeId = activityTypeId,
    name = name,

)

