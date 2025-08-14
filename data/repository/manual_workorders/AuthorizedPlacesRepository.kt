package com.lasec.monitoreoapp.data.repository.manual_workorders

import com.lasec.monitoreoapp.data.database.dao.manual_workorders.AuthorizedPlacesDao
import com.lasec.monitoreoapp.data.database.entities.manual_workorders.AuthorizedPlaceEntity
import javax.inject.Inject

class AuthorizedPlacesRepository @Inject constructor(private val authorizedPlacesDao: AuthorizedPlacesDao) {

    suspend fun insertAuthorizedPlacesToDatabase(authorizedPlaces: List<AuthorizedPlaceEntity>) {
        authorizedPlacesDao.insertAll(authorizedPlaces)
    }

    suspend fun clearAllAuthorizedPlaces() {
        authorizedPlacesDao.clearAllPlaces()
    }

}