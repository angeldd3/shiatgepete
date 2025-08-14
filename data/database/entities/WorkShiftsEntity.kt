package com.lasec.monitoreoapp.data.database.entities

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lasec.monitoreoapp.domain.model.WorkShift

@Entity(tableName = "workshifts")
data class WorkShiftEntity(
    @PrimaryKey val id: Int,
    val description: String,
    val startTime: String, // "HH:mm:ss"
    val endTime: String,   // "HH:mm:ss"
    val order: Int,
    val enabled: Boolean
)

fun WorkShift.toDatabase() = WorkShiftEntity(
    id = id,
    description = description,
    startTime = startTime,
    endTime = endTime,
    order = order,
    enabled = enabled
)