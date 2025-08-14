package com.lasec.monitoreoapp.data.database.dao.manual_workorders

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.TaskAssignmentEntity


@Dao
interface TaskAssignmentDao {

    @Insert
    suspend fun insertAssignment(taskAssignmentEntity: TaskAssignmentEntity): Long

    @Query("SELECT * FROM task_assignment_table WHERE assignmentLocalId = :assignmentLocalId")
    suspend fun getAssignmentByLocalId(assignmentLocalId: Int): TaskAssignmentEntity
}