package com.lasec.monitoreoapp.domain.usecase.incident_reports_usecase

import com.lasec.monitoreoapp.core.util.NetworkHelper
import com.lasec.monitoreoapp.data.database.entities.incident_entities.IncidentReportEntity
import com.lasec.monitoreoapp.data.repository.incident_reports_repository.IncidentReportsRepository
import javax.inject.Inject

class SyncIncidentReportsUseCase @Inject constructor(
    private val repository: IncidentReportsRepository,
    private val networkHelper: NetworkHelper // Detecta conectividad
) {

    suspend fun insertAndSyncIncidentReport(report: IncidentReportEntity) {
        // 1. Guardar localmente
        repository.insertIncidentReports(report)

        // 2. Intentar enviar si hay internet
        if (networkHelper.isNetworkConnected()) {
            try {
                repository.postIncidentReportsToApi(report)
                // 3. Si se envía correctamente, eliminar local
                repository.deleteIncidentReportsFromDatabase(report.id)
            } catch (e: Exception) {
                // Si falla, dejarlo en BD
                e.printStackTrace()
            }
        }
    }

    suspend fun resendPendingIncidentReports() {
        if (!networkHelper.isNetworkConnected()) return

        val pendingReports = repository.getAllIncidentReports()
        pendingReports.forEach { report ->
            try {
                repository.postIncidentReportsToApi(report)
                repository.deleteIncidentReportsFromDatabase(report.id)
            } catch (e: Exception) {
                // Si sigue fallando, lo dejamos
                e.printStackTrace()
            }
        }
    }
}
