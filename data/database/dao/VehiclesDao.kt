package com.lasec.monitoreoapp.data.database.dao

import androidx.room.*
import com.lasec.monitoreoapp.data.database.entities.VehiclesEntity

@Dao
interface VehiclesDao {

    @Query("SELECT * FROM vehicles_table ORDER BY economic_number ASC")
    suspend fun getAllVehicles(): List<VehiclesEntity>

    @Upsert
    suspend fun upsertVehicle(vehicles: List<VehiclesEntity>)

    @Query("DELETE FROM vehicles_table")
    suspend fun deleteAllVehicles()

    @Query("""
        SELECT * FROM vehicles_table
        WHERE vehicleTypeId = :vehicleTypeId
        AND indexVehicleId NOT IN (:excludedVehicleIds)
    """)
    suspend fun getAvailableVehicles(vehicleTypeId: Int, excludedVehicleIds: List<Int>): List<VehiclesEntity>
}
