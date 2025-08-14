package com.lasec.monitoreoapp.domain.usecase.incident_reports_usecase


import com.lasec.monitoreoapp.data.repository.incident_reports_repository.CategoriesRepository
import com.lasec.monitoreoapp.domain.model.incident_reports_model.Category
import com.lasec.monitoreoapp.domain.model.incident_reports_model.toDomain
import javax.inject.Inject

class GetCategoriesByActivityIdUseCase @Inject constructor(private val categoriesRepository: CategoriesRepository) {

    suspend operator fun invoke(activityId: Int): List<Category> {
        val response = categoriesRepository.getAllCategoriesFromDatabaseByActivityId(activityId)
        return response.map {it.toDomain()}
    }
}