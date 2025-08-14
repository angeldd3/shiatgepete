package com.lasec.monitoreoapp.data.repository.manual_workorders

import androidx.room.withTransaction
import com.lasec.monitoreoapp.data.database.MonitoringDatabase
import com.lasec.monitoreoapp.data.database.dao.manual_workorders.TaskAssignmentDao
import com.lasec.monitoreoapp.data.database.dao.manual_workorders.TaskPlanningDao
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.TaskAssignmentEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.TaskPlanningEntity
import javax.inject.Inject




class ManualWorkOrdersRepository @Inject constructor(
    private val db: MonitoringDatabase,
    private val taskAssignmentDao: TaskAssignmentDao,
    private val taskPlanningDao: TaskPlanningDao
) {
    suspend fun insertAssignmentWithPlanning(
        assignment: TaskAssignmentEntity,
        planningFactory: (Int) -> TaskPlanningEntity
    ) = db.withTransaction {
        val rowId = taskAssignmentDao.insertAssignment(assignment)
        val assignmentLocalId = rowId.toInt()
        taskPlanningDao.insertPlanning(planningFactory(assignmentLocalId))
    }

    suspend fun getAssignmentByLocalId(assignmentLocalId: Int): TaskAssignmentEntity {
        return taskAssignmentDao.getAssignmentByLocalId(assignmentLocalId)

    }

    suspend fun getPlanningsByAssignmentLocalId(assignmentLocalId: Int): List<TaskPlanningEntity> {
        return taskPlanningDao.getPlanningsByAssignmentLocalId(assignmentLocalId)
    }
}
