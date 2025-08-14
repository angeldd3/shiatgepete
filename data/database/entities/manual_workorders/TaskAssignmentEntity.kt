package com.lasec.monitoreoapp.data.database.entities.manual_workorders

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "task_assignment_table")
data class TaskAssignmentEntity(
    @PrimaryKey(autoGenerate = true) val assignmentLocalId: Int = 0,

    val indexEmployeeId: Int,
    val cloned: Boolean = false,
    val synced: Boolean = false,

    // Turno (workShift)
    val workShiftId: Int,
    val workShiftDescription: String,
    val workShiftStartHour: Int,
    val workShiftStartMinute: Int,
    val workShiftStartSecond: Int,
    val workShiftEndHour: Int,
    val workShiftEndMinute: Int,
    val workShiftEndSecond: Int
)