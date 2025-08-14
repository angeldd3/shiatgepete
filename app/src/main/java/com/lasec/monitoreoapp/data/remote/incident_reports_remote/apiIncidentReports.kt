package com.lasec.monitoreoapp.data.remote.incident_reports_remote


import com.lasec.monitoreoapp.domain.model.incident_reports_model.IncidentReportsDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface IncidentReportsApiService {
    @POST("service/persistent/api/v1/incidentreports/CreateIncidentReport")
    suspend fun postIncidentReport(
        @Body incidentReport: IncidentReportsDto
    ): Response<Unit> // o Response<Any> si esperas contenido
}