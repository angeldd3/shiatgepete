package com.lasec.monitoreoapp.data.database.dao.manual_workorders

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.TaskAssignmentEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.TaskPlanningEntity

@Dao
interface TaskPlanningDao {

    @Insert
    suspend fun insertPlanning(taskPlanningEntity: TaskPlanningEntity): Long

    @Query("SELECT * FROM task_planning_table WHERE assignmentLocalId = :assignmentLocalId")
    suspend fun getPlanningsByAssignmentLocalId(assignmentLocalId: Int): List<TaskPlanningEntity>
}