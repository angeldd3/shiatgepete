package com.lasec.monitoreoapp.data.database.dao

import androidx.room.*
import com.lasec.monitoreoapp.data.database.entities.ActivitiesEntity
import com.lasec.monitoreoapp.domain.model.ActivityOption

@Dao
interface ActivitiesDao {

    @Query("SELECT * FROM activities_table ORDER BY name ASC")
    suspend fun getAllActivities(): List<ActivitiesEntity>

    @Upsert
    suspend fun upsertActivities(activities: List<ActivitiesEntity>)

    @Query("DELETE FROM activities_table")
    suspend fun deleteAllActivities()

    @Query("""
        SELECT a.activityTypeId, a.name AS activityName
        FROM vehicles_table v
        INNER JOIN dispatch_vehicle_types_table dvt 
            ON v.vehicleTypeId = dvt.vehicleTypeId
        INNER JOIN activity_type_dispatch_vehicle_type_table atdvt 
            ON dvt.dispatchVehicleTypeId = atdvt.dispatchVehicleTypeId
        INNER JOIN activities_table a 
            ON atdvt.activityTypeId = a.activityTypeId
        WHERE v.indexVehicleId = :indexVehicleId
    """)
    suspend fun getActivitiesByVehicleId(indexVehicleId: Int): List<ActivityOption>
//    @Query("SELECT dispatchVehicleTypeId FROM activities_table WHERE activityTypeId = :activityTypeId")
//    suspend fun getDispatchVehicleTypeId(activityTypeId: Int): Int
}
