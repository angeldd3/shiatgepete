package com.lasec.monitoreoapp.data.database.entities.manual_workorders

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lasec.monitoreoapp.domain.model.Empleado


@Entity(tableName = "dispatch_vehicle_type_index_employee_table")
data class DispatchVehicleTypeIndexEmployeeEntity(
    @PrimaryKey val dispatchVehicleTypeIndexEmployeeId: Int,
    val dispatchVehicleTypeId: Int,
    val indexEmployeeId: Int
)

fun Empleado.toDatabaseList(): List<DispatchVehicleTypeIndexEmployeeEntity> {
    return indexEmployee.dispatchVehicleTypeIndexEmployees.map {
        DispatchVehicleTypeIndexEmployeeEntity(
            dispatchVehicleTypeIndexEmployeeId = it.dispatchVehicleTypeIndexEmployeeId,
            dispatchVehicleTypeId = it.dispatchVehicleTypeId,
            indexEmployeeId = it.indexEmployeeId
        )
    }
}

