package com.lasec.monitoreoapp.data.database.entities.manual_workorders

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lasec.monitoreoapp.domain.model.manual_workorders.AuthorizedVehicles

@Entity(tableName = "authorized_vehicles_table")
data class AuthorizedVehicleEntity(
    @PrimaryKey val authorizedVehicleId: String,
    val indexVehicleId: Int
)

fun AuthorizedVehicles.toDatabase() = AuthorizedVehicleEntity(
    authorizedVehicleId = authorizedVehicleId,
    indexVehicleId = indexVehicleId
)
