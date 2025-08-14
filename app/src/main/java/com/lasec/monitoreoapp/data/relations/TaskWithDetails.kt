package com.lasec.monitoreoapp.data.relations

import androidx.room.Embedded
import androidx.room.Relation
import com.lasec.monitoreoapp.data.database.entities.*
import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.ProgressLogEntityFromTasks


data class TaskWithRelations(
    @Embedded val task: TasksEntity,

    @Relation(
        parentColumn = "activity_id",
        entityColumn = "activityTypeId"
    )
    val activity: ActivitiesEntity,

    @Relation(
        parentColumn = "place_id",
        entityColumn = "id"
    )
    val place: PlacesEntity,

    @Relation(
        parentColumn = "vehicle_id",
        entityColumn = "indexVehicleId"
    )
    val vehicle: VehiclesEntity,

    @Relation(
        parentColumn = "order_id",
        entityColumn = "id"
    )
    val order: OrdersEntity,

    @Relation(
        parentColumn = "employee_id",
        entityColumn = "DispatchEmployeeId"
    )
    val employee: EmployeesEntity,

    @Relation(
        parentColumn = "task_planning_id",
        entityColumn = "taskPlanningId"
    )
    val progressLog: ProgressLogEntityFromTasks?,

    @Relation(
        parentColumn = "task_planning_id",
        entityColumn = "taskPlanningId",
        entity = ProgressLogEntityFromTasks::class
    )
    val progressLogWithStatus: ProgressLogWithStatus?

)



