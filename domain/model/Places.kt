package com.lasec.monitoreoapp.domain.model

import com.google.gson.annotations.SerializedName
import com.lasec.monitoreoapp.data.database.entities.PlacesEntity

data class Places(
    @SerializedName("PlaceId") val placeId: Int,
    @SerializedName("Name") val name: String,
    @SerializedName("Zone") val zone: Zone?
)

data class Zone(
    @SerializedName("Name") val name: String,
    @SerializedName("Level") val level: Level?
)

data class Level(
    @SerializedName("LevelId") val levelId: Int,
    @SerializedName("Name") val name: String
)


data class PlacesDomain(
    val id: Int,
    val name: String,
    val levelId: Int?,
    val levelText: String
)

fun PlacesEntity.toDomain(): PlacesDomain {
    return PlacesDomain(
        id = id,
        name = name,
        levelId = levelId,
        levelText = levelId?.let { "Nivel $it" } ?: "Sin nivel"
    )
}
