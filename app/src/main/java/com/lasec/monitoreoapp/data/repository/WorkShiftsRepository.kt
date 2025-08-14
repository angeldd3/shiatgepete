package com.lasec.monitoreoapp.data.repository


import com.lasec.monitoreoapp.data.database.dao.WorkShiftsDao
import com.lasec.monitoreoapp.data.database.entities.ShiftEntity
import com.lasec.monitoreoapp.data.database.entities.WorkShiftEntity
import com.lasec.monitoreoapp.data.database.entities.toShift
import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.domain.model.WorkShift
import com.lasec.monitoreoapp.domain.model.toDomain
import javax.inject.Inject
import kotlin.collections.map

class WorkShiftsRepository @Inject constructor(private val workShiftsDao: WorkShiftsDao) {

    suspend fun getAllWorkShiftsFromApi(): List<WorkShift> {
        val response = RetrofitInstance.workShiftsApi.getWorkShifts()
        return response.map { it.toDomain() }
    }

    suspend fun getAllWorkShiftsFromDatabase(): List<WorkShift> {
        val response: List<WorkShiftEntity> = workShiftsDao.getAll()
        return response.map { it.toDomain() }
    }

    suspend fun insertWorkShifts(workShifts: List<WorkShiftEntity>) {
        workShiftsDao.insertAll(workShifts)
    }

    suspend fun clearWorkShifts(){
        workShiftsDao.deleteAllWorkShifts()
    }

    suspend fun getById(workShiftId: Int): ShiftEntity{
        val response = workShiftsDao.getById(workShiftId)
        return response.toShift()
    }
}