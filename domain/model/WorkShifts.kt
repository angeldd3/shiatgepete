package com.lasec.monitoreoapp.domain.model

import com.google.gson.annotations.SerializedName
import com.lasec.monitoreoapp.data.database.entities.WorkShiftEntity

data class WorkShifts(
    @SerializedName("WorkShiftId") val id: Int,
    @SerializedName("Description") val description: String,
    @SerializedName("StartTime") val startTime: String, // "HH:mm:ss"
    @SerializedName("EndTime") val endTime: String,     // "HH:mm:ss"
    @SerializedName("Orden") val order: Int,
    @SerializedName("Enabled") val enabled: Boolean
)

data class WorkShift(
    val id: Int,
    val description: String,
    val startTime: String,
    val endTime: String,
    val order: Int,
    val enabled: Boolean
)

fun WorkShifts.toDomain() = WorkShift(
    id = id,
    description = description,
    startTime = startTime,
    endTime = endTime,
    order = order,
    enabled = enabled
)

fun WorkShiftEntity.toDomain() = WorkShift(
    id = id,
    description = description,
    startTime = startTime,
    endTime = endTime,
    order = order,
    enabled = enabled
)