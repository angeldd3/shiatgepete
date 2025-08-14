package com.lasec.monitoreoapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lasec.monitoreoapp.domain.model.Vehicles

@Entity(tableName = "vehicles_table")
data class VehiclesEntity(
    @PrimaryKey
    @ColumnInfo(name = "indexVehicleId") val indexVehicleId: Int,
    @ColumnInfo(name = "economic_number") val economicNumber: String,
    @ColumnInfo(name= "vehicleTypeId") val vehicleTypeId: Int

    )

fun Vehicles.toDatabase() = VehiclesEntity(
    indexVehicleId = indexVehicle.indexVehicleId,
    economicNumber = vehicleInfo.economicNumber,
    vehicleTypeId = vehicleInfo.vehicleTypeId

)

