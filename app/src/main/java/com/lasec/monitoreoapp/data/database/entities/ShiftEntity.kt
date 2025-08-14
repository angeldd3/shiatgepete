package com.lasec.monitoreoapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lasec.monitoreoapp.domain.model.WorkShift

@Entity(tableName = "shift_table")
data class ShiftEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "start_time") val startTime: String,
    @ColumnInfo(name = "end_time") val endTime: String
)


fun List<WorkShift>.toDatabaseShift(): List<ShiftEntity> {
    return this.map {
        ShiftEntity(
            id = it.id,
            description = it.description,
            startTime = it.startTime,
            endTime = it.endTime
        )
    }
}

fun WorkShiftEntity.toShift(): ShiftEntity {
    return ShiftEntity(
        id = this.id,
        description = this.description,
        startTime = this.startTime,
        endTime = this.endTime
    )
}

