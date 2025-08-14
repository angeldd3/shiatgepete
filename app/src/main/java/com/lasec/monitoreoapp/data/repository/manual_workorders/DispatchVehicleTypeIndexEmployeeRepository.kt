package com.lasec.monitoreoapp.data.repository.manual_workorders

import com.lasec.monitoreoapp.data.database.dao.manual_workorders.DispatchVehicleTypeIndexEmployeeDao
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.DispatchVehicleTypeIndexEmployeeEntity
import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.domain.model.manual_workorders.VehicleOption
import javax.inject.Inject

class DispatchVehicleTypeIndexEmployeeRepository @Inject constructor(private val dispatchVehicleTypeIndexEmployeeDao: DispatchVehicleTypeIndexEmployeeDao) {

    suspend fun upsertDispatchVehicleTypeIndexEmployeeToDatabase(dispatchVehicleTypeIndexEmployees: List<DispatchVehicleTypeIndexEmployeeEntity>) {
        dispatchVehicleTypeIndexEmployeeDao.upsertDispatchVehicleTypeIndexEmployee(
            dispatchVehicleTypeIndexEmployees
        )
    }

    suspend fun getVehiclesByIndexEmployeeFromDatabase(indexEmployeeId: Int): List<VehicleOption> {
        return dispatchVehicleTypeIndexEmployeeDao.getVehiclesByIndexEmployee(indexEmployeeId)
    }
}