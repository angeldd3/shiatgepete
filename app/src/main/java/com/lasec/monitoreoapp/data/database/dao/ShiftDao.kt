package com.lasec.monitoreoapp.data.database.dao

import androidx.room.*
import com.lasec.monitoreoapp.data.database.entities.ShiftEntity

@Dao
interface ShiftDao {

    @Query("SELECT * FROM shift_table ORDER BY description ASC")
    suspend fun getShift(): List<ShiftEntity>

    @Upsert
    suspend fun upsertShift(shifts: List<ShiftEntity>)

    @Query("DELETE FROM shift_table")
    suspend fun deleteShift()
}
