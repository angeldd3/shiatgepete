package com.lasec.monitoreoapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lasec.monitoreoapp.data.database.entities.WorkShiftEntity


@Dao
interface WorkShiftsDao {
    @Query("SELECT * FROM workshifts WHERE enabled = 1")
    suspend fun getAll(): List<WorkShiftEntity>


    @Insert(onConflict = OnConflictStrategy.IGNORE	)
    suspend fun insertAll(workShifts: List<WorkShiftEntity>)


    @Query("DELETE FROM workshifts")
    suspend fun deleteAllWorkShifts()


    @Query("SELECT * FROM workshifts WHERE id = :workShiftId LIMIT 1")
    suspend fun getById(workShiftId: Int): WorkShiftEntity
}