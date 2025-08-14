package com.lasec.monitoreoapp.data.repository.manual_workorders.create_workorders

import com.lasec.monitoreoapp.data.database.dao.manual_workorders.create_workorders.PlaceWorkOrderDao
import com.lasec.monitoreoapp.data.database.dao.manual_workorders.create_workorders.TaskPlanningPlaceLinkDao
import com.lasec.monitoreoapp.data.database.dao.manual_workorders.create_workorders.WorkOrderHeaderDao
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders.PlaceWorkOrderEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders.TaskPlanningPlaceLinkEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders.WorkOrderHeaderEntity
import javax.inject.Inject

class WorkOrdersResponseRepository @Inject constructor(
    private val placeWorkOrderDao: PlaceWorkOrderDao,
    private val taskPlanningPlaceLinkDao: TaskPlanningPlaceLinkDao,
    private val workOrderHeaderDao: WorkOrderHeaderDao
) {
    //Inserts
    suspend fun insertPlaceWorkOrderToDatabase(placeWorkOrder: List<PlaceWorkOrderEntity>) {
        placeWorkOrderDao.insertPlaceWorkOrders(placeWorkOrder)
    }

    suspend fun insertTaskPlanningPlaceLinkToDatabase(taskPlanningPlaceLink: List<TaskPlanningPlaceLinkEntity>) {
        taskPlanningPlaceLinkDao.insertTaskPlanningLinks(taskPlanningPlaceLink)
    }

    suspend fun insertWorkOrderHeaderToDatabase(workOrderHeader: WorkOrderHeaderEntity) {
        workOrderHeaderDao.insertWorkOrderHeader(workOrderHeader)
    }

    //Gets
    suspend fun getWorkOrderIdForAssignment(assignmentLocalId: Int): String? {
        return taskPlanningPlaceLinkDao.getWorkOrderIdForAssignment(assignmentLocalId)
    }

    suspend fun getPlaceWorkOrderId(workOrderId: String?, placeId: Int): String? {
        return placeWorkOrderDao.getPlaceWorkOrderId(workOrderId, placeId)
    }

    suspend fun getWorkOrderHeader(workOrderId: String): WorkOrderHeaderEntity? {
        return workOrderHeaderDao.getHeaderById(workOrderId)
    }

    }
