package com.lasec.monitoreoapp.domain.usecase.manual_workorders


import com.lasec.monitoreoapp.data.repository.manual_workorders.DispatchVehicleTypeIndexEmployeeRepository
import com.lasec.monitoreoapp.domain.model.manual_workorders.VehicleOption
import javax.inject.Inject

class GetVehiclesByIndexEmployeeUseCase @Inject constructor(
    private val dispatchVehiclesByIndexEmployeeRepository: DispatchVehicleTypeIndexEmployeeRepository
) {
    suspend operator fun invoke(indexEmployeeId: Int): List<VehicleOption> {
        return dispatchVehiclesByIndexEmployeeRepository.getVehiclesByIndexEmployeeFromDatabase(indexEmployeeId)
    }
}