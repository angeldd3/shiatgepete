package com.lasec.monitoreoapp.data.repository.manual_workorders

import com.lasec.monitoreoapp.data.database.dao.manual_workorders.ActivityTypeDispatchVehicleTypeDao
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.ActivityTypeDispatchVehicleTypeEntity
import javax.inject.Inject

class ActivityTypeDispatchVehicleTypeRepository @Inject constructor(private val activityTypeDispatchVehicleTypeDao: ActivityTypeDispatchVehicleTypeDao) {

    suspend fun upsertActivityTypeDispatchVehicleTypeRepository(
        activityTypeDispatchVehicleTypeRepository: List<ActivityTypeDispatchVehicleTypeEntity>
    ) {
        activityTypeDispatchVehicleTypeDao.upsertActivityTypeDispatchVehicleTypes(activityTypeDispatchVehicleTypeRepository)
    }
}