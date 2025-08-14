package com.lasec.monitoreoapp.data.remote

import com.lasec.monitoreoapp.domain.model.Users
import retrofit2.http.GET

interface UsersApiService {
    @GET("Catalog/GetUsers")
    suspend fun getAllUsers(): List<Users>
}