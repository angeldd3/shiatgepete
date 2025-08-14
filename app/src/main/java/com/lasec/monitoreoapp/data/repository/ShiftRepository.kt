package com.lasec.monitoreoapp.data.repository

import com.lasec.monitoreoapp.data.database.dao.ShiftDao
import com.lasec.monitoreoapp.data.database.entities.ShiftEntity
import com.lasec.monitoreoapp.data.database.entities.WorkShiftEntity
import com.lasec.monitoreoapp.domain.model.WorkShift
import com.lasec.monitoreoapp.data.database.entities.toDatabaseShift
import javax.inject.Inject

class ShiftRepository @Inject constructor(private val shiftDao: ShiftDao) {

    suspend fun upsertShift(shift: List<WorkShift> ){
        val shifts = shift.toDatabaseShift()
        return shiftDao.upsertShift(shifts)
    }
}


