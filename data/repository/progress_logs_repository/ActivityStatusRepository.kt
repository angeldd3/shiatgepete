package com.lasec.monitoreoapp.data.repository.progress_logs_repository

import com.lasec.monitoreoapp.data.database.dao.progress_logs_dao.ActivityStatusDao
import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.ActivityStatusEntity
import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.toDatabase
import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.domain.model.progress_logs_model.ActivityStatusDomain
import com.lasec.monitoreoapp.domain.model.progress_logs_model.toDomain
import javax.inject.Inject

class ActivityStatusRepository @Inject constructor(private val activityStatusDao: ActivityStatusDao) {

    suspend fun getAllActivityStatusFromApi(): List<ActivityStatusEntity> {
        val response = RetrofitInstance.activityStatusApi.getAllActivityStatus()
        return response.map { it.toDatabase() }
    }

    suspend fun upsertActivityStatus(activityStatus: List<ActivityStatusEntity>) {
        activityStatusDao.upsertActivityStatus(activityStatus)
    }

    suspend fun getActivityStatusByIdFromDatabase(id: Int): List<ActivityStatusDomain> {
        val response = activityStatusDao.getActivityStatusById(id)
        return response.map {it.toDomain()}
    }

}