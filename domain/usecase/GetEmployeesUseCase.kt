package com.lasec.monitoreoapp.domain.usecase

import android.util.Log
import com.lasec.monitoreoapp.data.EmployeesRepository
import com.lasec.monitoreoapp.data.database.entities.toDatabase
import com.lasec.monitoreoapp.domain.model.Employee
import com.lasec.monitoreoapp.domain.model.toDomain
import javax.inject.Inject

class GetEmployeesUseCase @Inject constructor(private val repository: EmployeesRepository) {


    suspend operator fun invoke(): List<Employee> {
        return try {
            // 1. Intentar obtener datos de la API
            val employeesDomain = repository.getAllEmployeesFromApi()
            val employees = employeesDomain.map { it.toDomain()}

            // 2. Si la API responde con datos
            if (employees.isNotEmpty()) {
                repository.clearEmployees()
                repository.insertEmployees(employees.map { it.toDatabase() })
                employees // Devolver datos frescos
            } else {
                // 3. Si la API devuelve vacío (¿error o datos válidos?)
                repository.getAllEmployeesFromDatabase() // Fallback a caché
            }
        } catch (e: Exception) {
            // 4. Manejar errores (red, servidor, etc.)
            Log.e("GetEmployeesUseCase", "Error al obtener empleados", e)
            // También puedes imprimirlo en consola si no tienes Log
            println("Error al obtener empleados: ${e.message}")
            repository.getAllEmployeesFromDatabase() // Usar datos locales
        }
    }


}