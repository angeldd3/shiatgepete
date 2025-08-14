package com.lasec.monitoreoapp.data.database.entities.manual_workorders

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lasec.monitoreoapp.data.database.entities.ActivitiesEntity
import com.lasec.monitoreoapp.domain.model.Activities
import com.lasec.monitoreoapp.domain.model.manual_workorders.ActivityTypeDispatchVehicleType

@Entity(
    tableName = "activity_type_dispatch_vehicle_type_table",
    foreignKeys = [
        ForeignKey(
            entity = DispatchVehicleTypesEntity::class,
            parentColumns = ["dispatchVehicleTypeId"],
            childColumns = ["dispatchVehicleTypeId"],
            onDelete = ForeignKey.CASCADE
        ),
        ForeignKey(
            entity = ActivitiesEntity::class,
            parentColumns = ["activityTypeId"],
            childColumns = ["activityTypeId"],
            onDelete = ForeignKey.CASCADE
        )

    ],
    indices = [
        Index(value = ["dispatchVehicleTypeId"]),
        Index(value = ["activityTypeId"]),
        // Opcional: evita duplicados de la pareja actividad-tipoVehículo
        Index(value = ["activityTypeId", "dispatchVehicleTypeId"], unique = true)
    ]
)
data class ActivityTypeDispatchVehicleTypeEntity(
    @PrimaryKey
    @ColumnInfo("activityTypeDispatchVehicleTypeId") val activityTypeDispatchVehicleTypeId: Int,
    @ColumnInfo("activityTypeId") val activityTypeId: Int,
    @ColumnInfo("dispatchVehicleTypeId") val dispatchVehicleTypeId: Int
)


fun ActivityTypeDispatchVehicleType.toActivityTypeDispatchVehicleTypeEntity() =
    ActivityTypeDispatchVehicleTypeEntity(
        activityTypeDispatchVehicleTypeId = activityTypeDispatchVehicleTypeId,
        activityTypeId = activityTypeId,
        dispatchVehicleTypeId = dispatchVehicleTypeId
    )