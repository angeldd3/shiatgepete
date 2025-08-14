package com.lasec.monitoreoapp.data.database.dao.manual_workorders

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.ActivityTypeDispatchVehicleTypeEntity

@Dao
interface ActivityTypeDispatchVehicleTypeDao {
    @Upsert
    suspend fun upsertActivityTypeDispatchVehicleTypes(activityTypeDispatchVehicleTypeEntity: List<ActivityTypeDispatchVehicleTypeEntity>)
}