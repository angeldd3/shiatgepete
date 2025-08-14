package com.lasec.monitoreoapp.data.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Upsert
import com.lasec.monitoreoapp.data.database.entities.UsersEntity
import com.lasec.monitoreoapp.data.database.entities.VehiclesEntity

@Dao
interface UsersDao {

    @Query("SELECT * FROM users_table ORDER BY full_name ASC")
    suspend fun getAllUsers(): List<UsersEntity>

    @Upsert
    suspend fun upsertUsers(users: UsersEntity): Long

    @Query("DELETE FROM users_table")
    suspend fun deleteAllUsers()
}