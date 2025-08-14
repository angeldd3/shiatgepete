package com.lasec.monitoreoapp.domain.usecase.manual_workorders

import com.lasec.monitoreoapp.data.repository.PlacesRepository
import com.lasec.monitoreoapp.domain.model.PlacesDomain
import javax.inject.Inject

class GetUnauthorizedPlacesUseCase @Inject constructor(
    private val placesRepository: PlacesRepository
) {
    suspend operator fun invoke(): List<PlacesDomain> {
        return placesRepository.getUnauthorizedPlacesFromDatabase()
    }
}
