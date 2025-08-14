package com.lasec.monitoreoapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lasec.monitoreoapp.domain.model.Employee

@Entity(tableName = "employees_table")
data class EmployeesEntity(
    @PrimaryKey
    @ColumnInfo(name = "DispatchEmployeeId") val DispatchEmployeeId: Int,
    @ColumnInfo(name = "IndexEmployeeId") val IndexEmployeeId: Int,
    @ColumnInfo(name = "Name") val Name: String,
    @ColumnInfo(name = "NoEmployee") val NoEmployee: String,
    @ColumnInfo(name = "PaternalLastName") val PaternalLastName: String,
    @ColumnInfo(name = "MaternalLastName") val MaternalLastName: String
)

fun Employee.toDatabase() = EmployeesEntity(
    Name = Name,
    PaternalLastName = PaternalLastName,
    NoEmployee = NoEmployee,
    MaternalLastName = MaternalLastName,
    DispatchEmployeeId = DispatchEmployeeId,
    IndexEmployeeId= IndexEmployeeId

)