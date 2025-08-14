package com.lasec.monitoreoapp.data.database.entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lasec.monitoreoapp.domain.model.Places

@Entity(tableName = "places_table")
data class PlacesEntity(
    @PrimaryKey
    @ColumnInfo(name = "id") val id: Int,
    @ColumnInfo(name = "Name") val name: String,
    @ColumnInfo(name = "LevelId") val levelId: Int?

)

fun Places.toDatabase() = PlacesEntity(
    id = placeId,
    name = name,
    levelId = zone?.level?.levelId
)
