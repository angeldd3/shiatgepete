package com.lasec.monitoreoapp.domain.repository

import com.lasec.monitoreoapp.domain.model.User

interface AuthRepository {
    suspend fun login(nombre: String, numero: String): User?
}