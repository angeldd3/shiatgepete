package com.lasec.monitoreoapp.data.repository

import com.lasec.monitoreoapp.data.database.dao.PlacesDao
import com.lasec.monitoreoapp.data.database.entities.PlacesEntity
import com.lasec.monitoreoapp.data.database.entities.toDatabase
import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.domain.model.Places
import com.lasec.monitoreoapp.domain.model.PlacesDomain
import com.lasec.monitoreoapp.domain.model.toDomain

import javax.inject.Inject

class PlacesRepository @Inject constructor(
    private val placesDao: PlacesDao
) {

    suspend fun getAllPlacesByIdFromApi(id: Int): Places {
        val response = RetrofitInstance.placesApi.getPlaceById(id)
        return response
    }

    suspend fun getAllPlacesFromApi(): List<PlacesEntity> {
        val response = RetrofitInstance.placesApi.getAllPlaces()
        return response.map { it.toDatabase() }
    }

    suspend fun upsert(place: List<PlacesEntity>) {
        return placesDao.upsertPlace(place)
    }

    suspend fun getUnauthorizedPlacesFromDatabase(): List<PlacesDomain> {
        val response = placesDao.getUnauthorizedPlaces()
        return response.map { it.toDomain() }
    }
}
