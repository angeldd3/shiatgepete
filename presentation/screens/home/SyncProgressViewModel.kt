package com.lasec.monitoreoapp.presentation.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.ProgressLogEntity
import com.lasec.monitoreoapp.domain.usecase.progress_logs_usecase.SyncProgressLogsUseCase
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProgressLogViewModel @Inject constructor(
    private val syncProgressLogsUseCase: SyncProgressLogsUseCase
) : ViewModel() {

    /**
     * Inserta el log localmente y trata de sincronizarlo si hay conexión
     */
    fun insertarYSincronizarLog(log: ProgressLogEntity) {
        viewModelScope.launch {
            try {
                syncProgressLogsUseCase.insertAndSyncProgressLog(log)
            } catch (e: Exception) {
                e.printStackTrace()
                Log.e("SyncProgressLog", "Error al sincronizar el log: ${e.message}")
            }
        }
    }

    /**
     * Reenvía todos los registros pendientes si hay conexión
     */
    fun reenviarLogsPendientes() {
        viewModelScope.launch {
            try {
                syncProgressLogsUseCase.resendPendingProgressLogs()
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
