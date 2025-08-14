package com.lasec.monitoreoapp.data.database.dao.manual_workorders

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.AuthorizedPlaceEntity

@Dao
interface AuthorizedPlacesDao {
    @Insert(onConflict = OnConflictStrategy.ABORT)
    suspend fun insertAll(places: List<AuthorizedPlaceEntity>)

    @Query("DELETE FROM authorized_places_table")
    suspend fun clearAllPlaces()
}