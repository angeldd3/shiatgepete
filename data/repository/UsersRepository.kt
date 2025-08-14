package com.lasec.monitoreoapp.data.repository

import com.lasec.monitoreoapp.data.database.dao.UsersDao
import com.lasec.monitoreoapp.data.database.entities.UsersEntity
import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.domain.model.Users
import javax.inject.Inject

class UsersRepository @Inject constructor(private val usersDao: UsersDao){

    suspend fun getAllUsers(): List<Users>{
        val response = RetrofitInstance.usersApi.getAllUsers()
        return response
    }

    suspend fun getUserById(id: String): Users? {
        val allUsers = getAllUsers()
        return allUsers.find { it.id == id }
    }

    suspend fun upsertUsers(users: UsersEntity): Long{
        return usersDao.upsertUsers(users)
    }
}