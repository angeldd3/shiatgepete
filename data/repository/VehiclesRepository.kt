package com.lasec.monitoreoapp.data.repository

import com.lasec.monitoreoapp.data.database.dao.VehiclesDao
import com.lasec.monitoreoapp.data.database.entities.VehiclesEntity
import com.lasec.monitoreoapp.data.database.entities.toDatabase
import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.domain.model.Vehicles
import javax.inject.Inject

class VehiclesRepository @Inject constructor(
    private val vehiclesDao: VehiclesDao
) {

    suspend fun getAllVehiclesFromApi(): List<VehiclesEntity> {
        val response = RetrofitInstance.vehiclesApi.getAllVehicles()
        return response.map {it.toDatabase()}
    }

//    suspend fun getByIndexVehicleId(indexVehicleId: Int): Vehicles? {
//        return getAllVehiclesFromApi().find { it.indexVehicle.indexVehicleId == indexVehicleId }
//    }

    suspend fun upsert(vehicles: List<VehiclesEntity>) {
        return vehiclesDao.upsertVehicle(vehicles)
    }
}
