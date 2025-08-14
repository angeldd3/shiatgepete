package com.lasec.monitoreoapp.data.database.entities.manual_workorders

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lasec.monitoreoapp.domain.model.manual_workorders.PlaceWorkOrder

@Entity(tableName = "authorized_places_table")
data class AuthorizedPlaceEntity(
    @PrimaryKey val placeWorkOrderId: String,
    val placeId: Int
)

fun PlaceWorkOrder.toDatabase(): AuthorizedPlaceEntity =
    AuthorizedPlaceEntity(
        placeWorkOrderId = this.placeWorkOrderId,
        placeId = this.placeId
    )