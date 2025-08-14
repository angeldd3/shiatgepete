package com.lasec.monitoreoapp.data.repository.manual_workorders

import com.lasec.monitoreoapp.data.database.dao.manual_workorders.DispatchVehicleTypesDao
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.DispatchVehicleTypesEntity
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.toDatabase
import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.domain.model.manual_workorders.DispatchVehicleTypes
import javax.inject.Inject

class DispatchVehicleTypesRepository @Inject constructor(private val dispatchVehicleTypesDao: DispatchVehicleTypesDao) {

    suspend fun getAllDispatchVehiclesTypesFromApi():List <DispatchVehicleTypesEntity>{
        val response = RetrofitInstance.dispatchVehiclesTypesApi.getAllDispatchVehicleTypes()
        return response.map {it.toDatabase()}
    }

    suspend fun upsertDispatchVehiclesTypes(dispatchVehiclesTypes: List<DispatchVehicleTypesEntity>){
        dispatchVehicleTypesDao.upsertDispatchVehiclesTypes(dispatchVehiclesTypes)
    }

    suspend fun getVehicleTypeIdFromDispatchVehicleTypes(dispatchVehicleTypeId: Int){
        dispatchVehicleTypesDao.getVehicleTypeIdFromDispatchVehicleTypes(dispatchVehicleTypeId)
    }

}