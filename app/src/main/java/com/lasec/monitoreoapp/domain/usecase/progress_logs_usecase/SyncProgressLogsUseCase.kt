package com.lasec.monitoreoapp.domain.usecase.progress_logs_usecase

import android.util.Log
import com.lasec.monitoreoapp.core.util.NetworkHelper
import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.ProgressLogEntity
import com.lasec.monitoreoapp.data.repository.progress_logs_repository.ProgressLogsRepository
import javax.inject.Inject

class SyncProgressLogsUseCase @Inject constructor(
    private val repository: ProgressLogsRepository,
    private val networkHelper: NetworkHelper
) {

    suspend fun insertAndSyncProgressLog(progressLog: ProgressLogEntity): Boolean {
        repository.insertProgressLogs(progressLog)

        if (networkHelper.isNetworkConnected()) {
            return try {
                val response = repository.postProgressLogsToApi(progressLog)
                if (response.isSuccessful) {
                    repository.deleteProgressLogsFromDatabase(progressLog.id)
                    Log.d("UseCase", "Log sincronizado correctamente")
                    true
                } else {
                    Log.e("UseCase", "Error al sincronizar: ${response.code()} ${response.message()}")
                    false
                }
            } catch (e: Exception) {
                Log.e("UseCase", "Excepción: ${e.message}", e)
                false
            }
        }
        return false
    }

    suspend fun resendPendingProgressLogs() {
        if (!networkHelper.isNetworkConnected()) return

        val pendingLogs = repository.getAllProgressLogsFromDatabase()
        pendingLogs.forEach { log ->
            try {
                repository.postProgressLogsToApi(log)
                repository.deleteProgressLogsFromDatabase(log.id)
            } catch (e: Exception) {
                e.printStackTrace() // Si sigue fallando, lo dejamos
            }
        }
    }
}
