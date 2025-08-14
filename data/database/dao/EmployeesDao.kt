package com.lasec.monitoreoapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lasec.monitoreoapp.data.database.entities.EmployeesEntity
import com.lasec.monitoreoapp.domain.model.Employee

@Dao
interface EmployeesDao {

    @Query("SELECT * FROM employees_table ORDER BY name ASC")
    suspend fun getAllEmployees(): List<EmployeesEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployees(employee: List<EmployeesEntity>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertEmployee(employee: EmployeesEntity)

    @Query("DELETE FROM employees_table")
    suspend fun deleteAllEmployees()

    @Query("SELECT * FROM employees_table WHERE DispatchEmployeeId = :dispatchId LIMIT 1")
    suspend fun getEmployeeByDispatchId(dispatchId: String): EmployeesEntity

    @Query("SELECT * FROM employees_table ORDER BY DispatchEmployeeId DESC LIMIT 3")
    suspend fun getUltimosTres(): List<EmployeesEntity>

    @Query("SELECT * FROM employees_table WHERE Name LIKE '%' || :query || '%'")
    suspend fun buscarPorNombre(query: String): List<EmployeesEntity>

    @Query(
        """
    SELECT * FROM employees_table
    WHERE LOWER(Name || ' ' || PaternalLastName || ' ' || MaternalLastName) = LOWER(:fullName)
    AND NoEmployee = :employeeNumber
    LIMIT 1
"""
    )
    suspend fun getEmployeeByNameAndNumber(
        fullName: String,
        employeeNumber: String
    ): EmployeesEntity?
}
