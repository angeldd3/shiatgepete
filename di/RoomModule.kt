package com.lasec.monitoreoapp.di

import android.content.Context
import androidx.room.Room
import com.lasec.monitoreoapp.data.database.MonitoringDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.annotation.Signed
import javax.inject.Singleton


@Module
@InstallIn(SingletonComponent::class)
object RoomModule {
    private const val MONITORING_DATABASE_NAME = "monitoring_database"

    @Singleton
    @Provides
    fun providesRoom(@ApplicationContext context: Context) =
        Room.databaseBuilder(context, MonitoringDatabase::class.java, MONITORING_DATABASE_NAME)
            .fallbackToDestructiveMigration()
            .build()

    @Singleton
    @Provides
    fun provideEmployeesDao(db: MonitoringDatabase)=db.getEmployeesDao()

    @Singleton
    @Provides
    fun provideWorkShiftsDao(db: MonitoringDatabase)=db.getWorkShiftsDao()

    @Singleton
    @Provides
    fun provideOrdersDao(db: MonitoringDatabase)=db.getOrdersDao()

    @Singleton
    @Provides
    fun provideShiftDao(db: MonitoringDatabase)=db.getShiftDao()

    @Singleton
    @Provides
    fun provideUsersDao(db: MonitoringDatabase)=db.getUsersDao()

    @Singleton
    @Provides
    fun provideVehiclesDao(db: MonitoringDatabase)=db.getVehiclesDao()

    @Singleton
    @Provides
    fun provideActivitiesDao(db: MonitoringDatabase)=db.getActivitiesDao()

    @Singleton
    @Provides
    fun providePlacesDao(db: MonitoringDatabase)=db.getPlacesDao()

    @Singleton
    @Provides
    fun provideTasksDao(db: MonitoringDatabase)=db.getTasksDao()

    @Singleton
    @Provides
    fun provideIncidentReportDao(db: MonitoringDatabase)=db.getIncidentReportDao()

    @Singleton
    @Provides
    fun provideCategoriesDao(db: MonitoringDatabase)=db.getCategoriesDao()

    @Singleton
    @Provides
    fun provideSubCategoriesDao(db: MonitoringDatabase)=db.getSubCategoriesDao()

    @Singleton
    @Provides
    fun provideDispatchVehiclesTypesDao(db: MonitoringDatabase)=db.getDispatchVehiclesTypesDao()

    @Singleton
    @Provides
    fun provideAuthorizedVehiclesDao(db: MonitoringDatabase)=db.getAuthorizedVehiclesDao()

    @Singleton
    @Provides
    fun provideProgressLogsDao(db: MonitoringDatabase)=db.getProgressLogsDao()

    @Singleton
    @Provides
    fun provideActivityStatusDao(db: MonitoringDatabase)=db.getActivityStatusDao()

    @Singleton
    @Provides
    fun provideActivityTypeDispatchVehicleTypeDao(db: MonitoringDatabase)=db.getActivityTypeDispatchVehicleTypeDao()

    @Singleton
    @Provides
    fun provideDispatchVehicleTypeIndexEmployeeDao(db: MonitoringDatabase)=db.getDispatchVehicleTypeIndexEmployeeDao()

    @Singleton
    @Provides
    fun provideProgressLogDaoFromTasks(db: MonitoringDatabase)=db.getProgressLogDaoFromTasks()

    @Singleton
    @Provides
    fun provideAuthorizedPlaces(db: MonitoringDatabase)=db.getAuthorizedPlacesDao()

    @Singleton
    @Provides
    fun provideTaskPlanningDao(db: MonitoringDatabase)=db.getTaskPlanningDao()

    @Singleton
    @Provides
    fun provideTaskAssignmentDao(db: MonitoringDatabase)=db.getTaskAssignmentDao()

    @Singleton
    @Provides
    fun providePlaceWorkOrderDao(db: MonitoringDatabase)=db.getPlaceWorkOrderDao()

    @Singleton
    @Provides
    fun provideTaskPlanningPlaceLinkDao(db: MonitoringDatabase)=db.getTaskPlanningPlaceLinkDao()

    @Singleton
    @Provides
    fun provideWorkOrderHeaderDao(db: MonitoringDatabase)=db.getWorkOrderHeaderDao()

    @Singleton
    @Provides
    fun provideTaskPlanningRemoteMapDao(db: MonitoringDatabase)=db.getTaskPlanningRemoteMapDao()
}