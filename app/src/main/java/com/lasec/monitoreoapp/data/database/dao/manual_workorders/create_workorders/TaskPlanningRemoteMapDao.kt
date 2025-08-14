package com.lasec.monitoreoapp.data.database.dao.manual_workorders.create_workorders

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders.TaskPlanningRemoteMapEntity

@Dao
interface TaskPlanningRemoteMapDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMap(entity: TaskPlanningRemoteMapEntity)

    @Query("SELECT taskPlanningIdRemote FROM task_planning_remote_map WHERE taskPlanningLocalId = :localId")
    suspend fun getRemoteId(localId: Int): String?
}
