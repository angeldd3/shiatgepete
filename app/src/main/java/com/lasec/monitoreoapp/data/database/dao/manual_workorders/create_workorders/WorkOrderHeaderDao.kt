package com.lasec.monitoreoapp.data.database.dao.manual_workorders.create_workorders

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders.WorkOrderHeaderEntity

@Dao
interface WorkOrderHeaderDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertWorkOrderHeader(entity: WorkOrderHeaderEntity)

    @Query("""
        SELECT workOrderId
        FROM work_orders_header
        WHERE workOrderId IN (
            SELECT DISTINCT workOrderId
            FROM task_planning_place_link
            WHERE taskPlanningLocalId IN (
                SELECT taskPlanningLocalId
                FROM task_planning_table
                WHERE assignmentLocalId = :assignmentLocalId
            )
        )
        LIMIT 1
    """)
    suspend fun getWorkOrderIdForAssignment(assignmentLocalId: Int): String?
}