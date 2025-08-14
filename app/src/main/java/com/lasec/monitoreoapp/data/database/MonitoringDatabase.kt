package com.lasec.monitoreoapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import com.lasec.monitoreoapp.data.database.dao.ActivitiesDao
import com.lasec.monitoreoapp.data.database.dao.EmployeesDao
import com.lasec.monitoreoapp.data.database.dao.incident_reports_dao.IncidentReportDao
import com.lasec.monitoreoapp.data.database.dao.PlacesDao
import com.lasec.monitoreoapp.data.database.dao.OrdersDao
import com.lasec.monitoreoapp.data.database.dao.TasksDao
import com.lasec.monitoreoapp.data.database.dao.ShiftDao
import com.lasec.monitoreoapp.data.database.dao.UsersDao
import com.lasec.monitoreoapp.data.database.dao.VehiclesDao
import com.lasec.monitoreoapp.data.database.dao.WorkShiftsDao
import com.lasec.monitoreoapp.data.database.dao.incident_reports_dao.CategoriesDao
import com.lasec.monitoreoapp.data.database.dao.incident_reports_dao.SubCategoriesDao
import com.lasec.monitoreoapp.data.database.dao.manual_workorders.ActivityTypeDispatchVehicleTypeDao
import com.lasec.monitoreoapp.data.database.dao.manual_workorders.AuthorizedPlacesDao
import com.lasec.monitoreoapp.data.database.dao.manual_workorders.AuthorizedVehiclesDao
import com.lasec.monitoreoapp.data.database.dao.manual_workorders.DispatchVehicleTypeIndexEmployeeDao
import com.lasec.monitoreoapp.data.database.dao.manual_workorders.DispatchVehicleTypesDao
import com.lasec.monitoreoapp.data.database.dao.manual_workorders.TaskAssignmentDao
import com.lasec.monitoreoapp.data.database.dao.manual_workorders.TaskPlanningDao
import com.lasec.monitoreoapp.data.database.dao.manual_workorders.create_workorders.PlaceWorkOrderDao
import com.lasec.monitoreoapp.data.database.dao.manual_workorders.create_workorders.TaskPlanningPlaceLinkDao
import com.lasec.monitoreoapp.data.database.dao.manual_workorders.create_workorders.TaskPlanningRemoteMapDao
import com.lasec.monitoreoapp.data.database.dao.manual_workorders.create_workorders.WorkOrderHeaderDao
import com.lasec.monitoreoapp.data.database.dao.progress_logs_dao.ActivityStatusDao
import com.lasec.monitoreoapp.data.database.dao.progress_logs_dao.ProgressLogDaoFromTasks
import com.lasec.monitoreoapp.data.database.dao.progress_logs_dao.ProgressLogsDao
import com.lasec.monitoreoapp.data.database.entities.ActivitiesEntity
import com.lasec.monitoreoapp.data.database.entities.EmployeesEntity
import com.lasec.monitoreoapp.data.database.entities.PlacesEntity
import com.lasec.monitoreoapp.data.database.entities.OrdersEntity
import com.lasec.monitoreoapp.data.database.entities.TasksEntity
import com.lasec.monitoreoapp.data.database.entities.ShiftEntity
import com.lasec.monitoreoapp.data.database.entities.UsersEntity
import com.lasec.monitoreoapp.data.database.entities.VehiclesEntity
import com.lasec.monitoreoapp.data.database.entities.WorkShiftEntity
import com.lasec.monitoreoapp.data.database.entities.incident_entities.CategoriesEntity
import com.lasec.monitoreoapp.data.database.entities.incident_entities.IncidentReportEntity
import com.lasec.monitoreoapp.data.database.entities.incident_entities.SubCategoriesEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.ActivityTypeDispatchVehicleTypeEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.AuthorizedPlaceEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.AuthorizedVehicleEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.DispatchVehicleTypeIndexEmployeeEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.DispatchVehicleTypesEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.TaskAssignmentEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.TaskPlanningEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders.PlaceWorkOrderEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders.TaskPlanningPlaceLinkEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders.TaskPlanningRemoteMapEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.create_workorders.WorkOrderHeaderEntity
import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.ActivityStatusEntity
import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.ProgressLogEntity
import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.ProgressLogEntityFromTasks


@Database(
    entities = [
        EmployeesEntity::class,
        OrdersEntity::class,
        ShiftEntity::class,
        VehiclesEntity::class,
        PlacesEntity::class,
        ActivitiesEntity::class,
        TasksEntity::class,
        WorkShiftEntity::class,
        UsersEntity::class,
        CategoriesEntity::class,
        IncidentReportEntity::class,
        SubCategoriesEntity::class,
        DispatchVehicleTypesEntity::class,
        AuthorizedVehicleEntity::class,
        ProgressLogEntity::class,
        ActivityStatusEntity::class,
        ActivityTypeDispatchVehicleTypeEntity::class,
        DispatchVehicleTypeIndexEmployeeEntity::class,
        ProgressLogEntityFromTasks::class,
        AuthorizedPlaceEntity::class,
        TaskPlanningEntity::class,
        TaskAssignmentEntity::class,
        PlaceWorkOrderEntity::class,
        TaskPlanningPlaceLinkEntity::class,
        WorkOrderHeaderEntity::class,
        TaskPlanningRemoteMapEntity::class
    ], version = 1
)
abstract class MonitoringDatabase : RoomDatabase() {

    abstract fun getEmployeesDao(): EmployeesDao

    abstract fun getOrdersDao(): OrdersDao

    abstract fun getShiftDao(): ShiftDao

    abstract fun getVehiclesDao(): VehiclesDao

    abstract fun getPlacesDao(): PlacesDao

    abstract fun getActivitiesDao(): ActivitiesDao

    abstract fun getTasksDao(): TasksDao

    abstract fun getUsersDao(): UsersDao

    abstract fun getWorkShiftsDao(): WorkShiftsDao

    abstract fun getIncidentReportDao(): IncidentReportDao

    abstract fun getCategoriesDao(): CategoriesDao

    abstract fun getSubCategoriesDao(): SubCategoriesDao

    abstract fun getDispatchVehiclesTypesDao(): DispatchVehicleTypesDao

    abstract fun getAuthorizedVehiclesDao(): AuthorizedVehiclesDao

    abstract fun getProgressLogsDao(): ProgressLogsDao

    abstract fun getActivityStatusDao(): ActivityStatusDao

    abstract fun getActivityTypeDispatchVehicleTypeDao(): ActivityTypeDispatchVehicleTypeDao

    abstract fun getDispatchVehicleTypeIndexEmployeeDao(): DispatchVehicleTypeIndexEmployeeDao

    abstract fun getProgressLogDaoFromTasks(): ProgressLogDaoFromTasks

    abstract fun getAuthorizedPlacesDao(): AuthorizedPlacesDao

    abstract fun getTaskPlanningDao(): TaskPlanningDao

    abstract fun getTaskAssignmentDao(): TaskAssignmentDao

    abstract fun getPlaceWorkOrderDao(): PlaceWorkOrderDao

    abstract fun getTaskPlanningPlaceLinkDao(): TaskPlanningPlaceLinkDao

    abstract fun getWorkOrderHeaderDao(): WorkOrderHeaderDao

    abstract fun getTaskPlanningRemoteMapDao(): TaskPlanningRemoteMapDao
}
