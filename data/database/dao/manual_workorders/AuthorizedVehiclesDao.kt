package com.lasec.monitoreoapp.data.database.dao.manual_workorders

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.AuthorizedVehicleEntity

@Dao
interface AuthorizedVehiclesDao {
    @Query("SELECT indexVehicleId FROM authorized_vehicles_table")
    suspend fun getAuthorizedVehicleIds(): List<Int>

    @Upsert
    suspend fun upsertAuthorizedVehicles(authorizedVehicles: List<AuthorizedVehicleEntity>)

    @Query("DELETE FROM authorized_vehicles_table")
    suspend fun clearAllAuthorizedVehicles()
}