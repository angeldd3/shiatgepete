package com.lasec.monitoreoapp.data

import com.lasec.monitoreoapp.data.database.dao.EmployeesDao
import com.lasec.monitoreoapp.data.database.entities.EmployeesEntity
import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.domain.model.Empleado
import com.lasec.monitoreoapp.domain.model.Employee
import com.lasec.monitoreoapp.domain.model.toDomain
import javax.inject.Inject

class EmployeesRepository  @Inject constructor(private val employeeDao: EmployeesDao){



    suspend fun getAllEmployeesFromApi(): List<Empleado> {
        val response = RetrofitInstance.employeeApi.getEmployees()
        return response
    }


    suspend fun getAllEmployeesFromDatabase(): List<Employee> {
        val response: List<EmployeesEntity> = employeeDao.getAllEmployees()
        return response.map { it.toDomain() }
    }

    suspend fun insertEmployees(employee: List<EmployeesEntity>){
        employeeDao.insertEmployees(employee)
    }

    suspend fun clearEmployees() {
        employeeDao.deleteAllEmployees()
    }
}