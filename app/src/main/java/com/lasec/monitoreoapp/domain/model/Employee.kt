package com.lasec.monitoreoapp.domain.model

import com.lasec.monitoreoapp.data.database.entities.EmployeesEntity

data class Employee(
    val Name: String,
    val PaternalLastName: String,
    val MaternalLastName: String,
    val NoEmployee: String,
    val DispatchEmployeeId: Int,
    val IndexEmployeeId: Int
)

fun Empleado.toDomain() = Employee(
    Name = employee.name,
    PaternalLastName = employee.paternalLastName,
    MaternalLastName = employee.maternalLastName,
    NoEmployee = employee.noEmployee,
    DispatchEmployeeId = dispatchEmployeeId,
    IndexEmployeeId = indexEmployee.indexEmployeeId
)

fun EmployeesEntity.toDomain() =
    Employee(Name, PaternalLastName, MaternalLastName, NoEmployee, DispatchEmployeeId,IndexEmployeeId )
