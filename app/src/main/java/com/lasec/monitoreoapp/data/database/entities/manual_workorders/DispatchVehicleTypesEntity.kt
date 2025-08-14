package com.lasec.monitoreoapp.data.database.entities.manual_workorders

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey
import com.lasec.monitoreoapp.data.database.entities.VehiclesEntity
import com.lasec.monitoreoapp.domain.model.manual_workorders.DispatchVehicleTypes

@Entity(
    tableName = "dispatch_vehicle_types_table"
)
data class DispatchVehicleTypesEntity(
    @PrimaryKey val dispatchVehicleTypeId: Int,
    val vehicleTypeId: Int
)

fun DispatchVehicleTypes.toDatabase() = DispatchVehicleTypesEntity(
    dispatchVehicleTypeId = dispatchVehicleTypeId,
    vehicleTypeId = vehicleTypeId
)