package com.lasec.monitoreoapp.domain.usecase.manual_workorders

import com.lasec.monitoreoapp.data.repository.ActivitiesRepository
import com.lasec.monitoreoapp.domain.model.ActivityOption
import javax.inject.Inject

class GetActivitiesByVehicleIdUseCase @Inject constructor(
    private val activitiesRepository: ActivitiesRepository
) {
    suspend operator fun invoke(indexVehicleId: Int): List<ActivityOption> {
        return activitiesRepository.getActivitiesByVehicleIdFromDatabase(indexVehicleId)
    }
}