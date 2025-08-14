package com.lasec.monitoreoapp.data.repository.incident_reports_repository

import com.lasec.monitoreoapp.data.database.dao.incident_reports_dao.IncidentReportDao
import com.lasec.monitoreoapp.data.database.entities.incident_entities.IncidentReportEntity
import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.domain.model.incident_reports_model.toDto
import javax.inject.Inject

class IncidentReportsRepository @Inject constructor(private val incidentReportDao: IncidentReportDao) {

    suspend fun insertIncidentReports(incidentReports: IncidentReportEntity) {
        return incidentReportDao.insertIncidentReports(incidentReports)
    }

    suspend fun deleteIncidentReportsFromDatabase(id: Int) {
        incidentReportDao.deleteById(id)
    }

    suspend fun postIncidentReportsToApi(incidentReports: IncidentReportEntity) {
        val incidentReportsDto = incidentReports.toDto()
        RetrofitInstance.incidentReportsApi.postIncidentReport(incidentReportsDto)
    }

    suspend fun getAllIncidentReports(): List<IncidentReportEntity> {
        return incidentReportDao.getAllIncidentReports()
    }

}