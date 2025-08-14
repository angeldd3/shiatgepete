import com.lasec.monitoreoapp.domain.model.Employee
import com.lasec.monitoreoapp.data.database.entities.EmployeesEntity

fun Employee.toEntity(): EmployeesEntity {
    return EmployeesEntity(
        Name = this.Name,
        PaternalLastName = this.PaternalLastName,
        MaternalLastName = this.MaternalLastName,
        DispatchEmployeeId = this.DispatchEmployeeId,
        IndexEmployeeId = this.IndexEmployeeId,
        NoEmployee = this.NoEmployee
    )
}

