package com.lasec.monitoreoapp.data.database.dao.manual_workorders.create_workorders

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders.TaskPlanningPlaceLinkEntity

@Dao
interface TaskPlanningPlaceLinkDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTaskPlanningLinks(links: List<TaskPlanningPlaceLinkEntity>)

    @Query("""
        SELECT DISTINCT link.workOrderId
        FROM task_planning_place_link AS link
        INNER JOIN task_planning_table AS tp
            ON tp.taskPlanningLocalId = link.taskPlanningLocalId
        WHERE tp.assignmentLocalId = :assignmentLocalId
        LIMIT 1
    """)
    suspend fun getWorkOrderIdForAssignment(assignmentLocalId: Int): String?
}