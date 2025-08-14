package com.lasec.monitoreoapp.data.database.dao

import androidx.room.*
import com.lasec.monitoreoapp.data.database.entities.PlacesEntity

@Dao
interface PlacesDao {

    @Query("SELECT * FROM places_table ORDER BY Name ASC")
    suspend fun getAllPlaces(): List<PlacesEntity>

    @Upsert
    suspend fun upsertPlace(places: List<PlacesEntity>)

    @Query("DELETE FROM places_table")
    suspend fun deleteAllPlaces()

    @Query("""
    SELECT p.*
    FROM places_table AS p
    WHERE NOT EXISTS (
        SELECT 1
        FROM authorized_places_table AS a
        WHERE a.`placeId` = p.`id`
    )
""")
    suspend fun getUnauthorizedPlaces(): List<PlacesEntity>


}
