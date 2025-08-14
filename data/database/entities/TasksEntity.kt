package com.lasec.monitoreoapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tasks_table",
    foreignKeys = [
        ForeignKey(
            entity = OrdersEntity::class,
            parentColumns = ["id"],
            childColumns = ["order_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ActivitiesEntity::class,
            parentColumns = ["activityTypeId"],
            childColumns = ["activity_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = PlacesEntity::class,
            parentColumns = ["id"],
            childColumns = ["place_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = VehiclesEntity::class,
            parentColumns = ["indexVehicleId"],
            childColumns = ["vehicle_id"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = EmployeesEntity::class,
            parentColumns = ["DispatchEmployeeId"],
            childColumns = ["employee_id"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(value = ["order_id"]),
        Index(value = ["activity_id"]),
        Index(value = ["place_id"]),
        Index(value = ["vehicle_id"]),
        Index(value = ["employee_id"])
    ]
)
data class TasksEntity(
    @PrimaryKey
    @ColumnInfo(name = "task_planning_id") val taskPlanningId: Int,
    @ColumnInfo(name = "order_id") val orderId: Int,
    @ColumnInfo(name = "activity_id") val activityId: Int,
    @ColumnInfo(name = "place_id") val placeId: Int,
    @ColumnInfo(name = "vehicle_id") val vehicleId: Int,
    @ColumnInfo(name = "quantity") val quantity: Float,
    @ColumnInfo(name = "init_time") val initTime: String,
    @ColumnInfo(name = "end_time") val endTime: String,
    @ColumnInfo(name = "employee_id") val dispatchEmployeeId: Int
)
