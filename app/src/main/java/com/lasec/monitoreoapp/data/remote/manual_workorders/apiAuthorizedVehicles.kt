package com.lasec.monitoreoapp.data.remote.manual_workorders

import com.lasec.monitoreoapp.domain.model.manual_workorders.AuthorizedVehicles
import com.lasec.monitoreoapp.domain.model.manual_workorders.AuthorizedVehiclesDto
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthorizedVehiclesApiService {
    @GET("service/dispatch/api/v1/AuthorizedVehicles")
    suspend fun getAllAuthorizedVehicles(): List<AuthorizedVehicles>

    @POST("service/dispatch/api/v1/AuthorizedVehicles")
    suspend fun postAuthorizedVehicles(
        @Body authorizedVehicles: AuthorizedVehiclesDto
    ): Response<Unit>
}