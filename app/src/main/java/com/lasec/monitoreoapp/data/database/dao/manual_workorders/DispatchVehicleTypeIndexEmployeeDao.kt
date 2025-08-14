package com.lasec.monitoreoapp.data.database.dao.manual_workorders

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.DispatchVehicleTypeIndexEmployeeEntity
import com.lasec.monitoreoapp.domain.model.manual_workorders.VehicleOption

@Dao
interface DispatchVehicleTypeIndexEmployeeDao {

    @Query("""
    SELECT v.indexVehicleId, v.economic_number, v.vehicleTypeId
    FROM dispatch_vehicle_type_index_employee_table ie
    INNER JOIN dispatch_vehicle_types_table dvt ON ie.dispatchVehicleTypeId = dvt.dispatchVehicleTypeId
    INNER JOIN vehicles_table v ON dvt.vehicleTypeId = v.vehicleTypeId
    WHERE ie.indexEmployeeId = :indexEmployeeId
      AND v.indexVehicleId NOT IN (
        SELECT av.indexVehicleId FROM authorized_vehicles_table av
      )
""")
    suspend fun getVehiclesByIndexEmployee(indexEmployeeId: Int): List<VehicleOption>


    @Upsert
    suspend fun upsertDispatchVehicleTypeIndexEmployee(dispatchVehicleTypeIndexEmployee: List<DispatchVehicleTypeIndexEmployeeEntity>)
}