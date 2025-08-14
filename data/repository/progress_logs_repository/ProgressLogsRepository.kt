package com.lasec.monitoreoapp.data.repository.progress_logs_repository

import com.lasec.monitoreoapp.data.database.dao.progress_logs_dao.ProgressLogsDao
import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.ProgressLogEntity
import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.data.remote.dto.ProgressLogResponse
import com.lasec.monitoreoapp.domain.model.progress_logs_model.toDto
import retrofit2.Response
import javax.inject.Inject

class ProgressLogsRepository @Inject constructor(private val progressLogsDao: ProgressLogsDao) {

    suspend fun insertProgressLogs(progressLogs: ProgressLogEntity) {
        return progressLogsDao.insertProgressLogs(progressLogs)
    }

    suspend fun deleteProgressLogsFromDatabase(id: Int) {
        progressLogsDao.deleteById(id)
    }

    suspend fun postProgressLogsToApi(progressLogs: ProgressLogEntity): Response<ProgressLogResponse> {
        val progressLogsDto = progressLogs.toDto()
        return RetrofitInstance.progressLogsApi.postProgressLogs(progressLogsDto)
    }

    suspend fun getAllProgressLogsFromDatabase(): List<ProgressLogEntity> {
        return progressLogsDao.getAllProgressLogs()
    }
}