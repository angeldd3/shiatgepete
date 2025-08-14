package com.lasec.monitoreoapp.data.database.dao.incident_reports_dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Upsert
import com.lasec.monitoreoapp.data.database.entities.incident_entities.IncidentReportEntity

@Dao
interface IncidentReportDao {

    @Query("SELECT * FROM incident_reports_table ORDER BY task_planning_id ASC")
    suspend fun getAllIncidentReportsById(): List<IncidentReportEntity>

    @Insert
    suspend fun insertIncidentReports(incidentReports: IncidentReportEntity)

    @Query("DELETE FROM incident_reports_table WHERE id = :id")
    suspend fun deleteById(id: Int)

    @Query("SELECT * FROM incident_reports_table ORDER BY id")
    suspend fun getAllIncidentReports(): List<IncidentReportEntity>
}