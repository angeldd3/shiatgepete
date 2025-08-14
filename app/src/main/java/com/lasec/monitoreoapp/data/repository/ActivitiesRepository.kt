package com.lasec.monitoreoapp.data.repository

import com.lasec.monitoreoapp.data.database.dao.ActivitiesDao
import com.lasec.monitoreoapp.data.database.entities.ActivitiesEntity
import com.lasec.monitoreoapp.data.database.entities.toDatabase
import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.domain.model.Activities
import com.lasec.monitoreoapp.domain.model.ActivityOption
import javax.inject.Inject

class ActivitiesRepository @Inject constructor(private val activitiesDao: ActivitiesDao) {

//    suspend fun getActivitiesByIdFromApi(id: Int): Activities {
//        val response = RetrofitInstance.activitiesApi.getActivitiesById(id)
//        return response
//    }
//
//    suspend fun upsert(activity: ActivitiesEntity): Long{
//        return activitiesDao.upsertActivities(activity)
//    }

    suspend fun getAllActivitiesFromApi(): List<Activities>{
        return RetrofitInstance.activitiesApi.getAllActivities()

    }

    suspend fun upsert(activities: List<ActivitiesEntity>){
        return activitiesDao.upsertActivities(activities)
    }

    suspend fun getActivitiesByVehicleIdFromDatabase(indexVehicleId: Int): List<ActivityOption>{
        return activitiesDao.getActivitiesByVehicleId(indexVehicleId)
    }

}