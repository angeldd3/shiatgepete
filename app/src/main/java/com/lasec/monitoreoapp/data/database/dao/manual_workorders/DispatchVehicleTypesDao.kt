package com.lasec.monitoreoapp.data.database.dao.manual_workorders

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.DispatchVehicleTypesEntity

@Dao
interface DispatchVehicleTypesDao {
    @Query("SELECT vehicleTypeId FROM dispatch_vehicle_types_table WHERE dispatchVehicleTypeId = :dispatchVehicleTypeId")
    suspend fun getVehicleTypeIdFromDispatchVehicleTypes(dispatchVehicleTypeId: Int): Int

    @Upsert
    suspend fun upsertDispatchVehiclesTypes(dispatchVehiclesTypes: List<DispatchVehicleTypesEntity>)
}