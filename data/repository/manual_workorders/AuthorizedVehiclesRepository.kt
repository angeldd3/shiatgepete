package com.lasec.monitoreoapp.data.repository.manual_workorders

import com.lasec.monitoreoapp.data.database.dao.manual_workorders.AuthorizedVehiclesDao
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.AuthorizedVehicleEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.toDatabase
import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.domain.model.manual_workorders.AuthorizedVehiclesDto
import retrofit2.Response
import javax.inject.Inject
import kotlin.Int
import kotlin.collections.List

class AuthorizedVehiclesRepository @Inject constructor(private val authorizedVehiclesDao: AuthorizedVehiclesDao) {

    suspend fun getAllAuthorizedVehiclesFromApi(): List<AuthorizedVehicleEntity> {
        val response = RetrofitInstance.authorizedVehiclesApi.getAllAuthorizedVehicles()
        return response.map { it.toDatabase() }
    }

    suspend fun upsertAuthorizedVehiclesToDatabase(authorizedVehicles: List<AuthorizedVehicleEntity>) {
        authorizedVehiclesDao.upsertAuthorizedVehicles(authorizedVehicles)
    }

    suspend fun getAuthorizedVehiclesIdsFromDatabase(): List<Int> {
        return authorizedVehiclesDao.getAuthorizedVehicleIds()
    }

    suspend fun clearAllAuthorizedVehiclesFromDatabase() {
        authorizedVehiclesDao.clearAllAuthorizedVehicles()
    }

    suspend fun postAuthorizedVehicles(
        workOrderId: String,
        indexVehicleId: Int
    ): Response<Unit> {
        val body = AuthorizedVehiclesDto(
            workOrderId = workOrderId,
            indexVehicleId = indexVehicleId

        )
        return RetrofitInstance.authorizedVehiclesApi.postAuthorizedVehicles(body)
    }
}