package com.lasec.monitoreoapp.domain.usecase.incident_reports_usecase

import com.lasec.monitoreoapp.data.database.entities.incident_entities.SubCategoriesEntity
import com.lasec.monitoreoapp.data.repository.incident_reports_repository.SubCategoriesRepository
import com.lasec.monitoreoapp.domain.model.incident_reports_model.SubCategory
import com.lasec.monitoreoapp.domain.model.incident_reports_model.toDomain
import javax.inject.Inject

class GetSubCategoriesByCategoryIdUseCase @Inject constructor(
    private val subCategoriesRepository: SubCategoriesRepository
) {
    suspend operator fun invoke(categoryId: Int): List<SubCategory> {
        val response = subCategoriesRepository.getSubCategoriesByCategoryId(categoryId)
        return response.map { it.toDomain() }
    }
}