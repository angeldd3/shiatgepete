package com.lasec.monitoreoapp.data.database.dao

import androidx.room.*
import com.lasec.monitoreoapp.data.database.entities.TasksEntity

import com.lasec.monitoreoapp.data.relations.TaskWithRelations

@Dao
interface TasksDao {

    @Query("SELECT * FROM tasks_table ORDER BY init_time DESC")
    suspend fun getAllTasks(): List<TasksEntity>

    @Upsert
    suspend fun upsertTasks(tasks: TasksEntity)

    @Query("DELETE FROM tasks_table")
    suspend fun deleteAllTasks()

    @Transaction
    @Query("SELECT * FROM tasks_table")
    suspend fun getTasksWithDetails(): List<TaskWithRelations>

    @Transaction
    @Query("SELECT * FROM tasks_table WHERE employee_id = :employeeId")
    suspend fun getTasksWithDetailsByEmployeeId(employeeId: Int): List<TaskWithRelations>

}
