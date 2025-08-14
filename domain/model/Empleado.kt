package com.lasec.monitoreoapp.domain.model

import com.lasec.monitoreoapp.domain.model.manual_workorders.DispatchVehicleTypeIndexEmployees

data class Empleado(
    val employee: EmployeeApi,
    val dispatchEmployeeId: Int,
    val indexEmployee: IndexEmployeeApi
)

data class EmployeeApi(
    val name: String,
    val paternalLastName: String,
    val maternalLastName: String,
    val noEmployee: String
)

data class IndexEmployeeApi(
    val indexEmployeeId: Int,
    val dispatchVehicleTypeIndexEmployees: List<DispatchVehicleTypeIndexEmployees>
)


