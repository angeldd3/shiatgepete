package com.lasec.monitoreoapp.data.repository

import com.lasec.monitoreoapp.data.database.dao.EmployeesDao
import com.lasec.monitoreoapp.domain.model.User
import com.lasec.monitoreoapp.domain.repository.AuthRepository
import javax.inject.Inject

class AuthRepositoryImpl @Inject constructor(
    private val employeesDao: EmployeesDao
) : AuthRepository {
    override suspend fun login(nombre: String, numero: String): User? {
        val empleado = employeesDao.getEmployeeByNameAndNumber(nombre.trim(), numero)
        return empleado?.let {
            User(nombre = it.Name, "Angel")
        }
    }
}
