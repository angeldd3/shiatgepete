package com.lasec.monitoreoapp.domain.usecase.incident_reports_usecase

import com.lasec.monitoreoapp.data.database.entities.incident_entities.toCategoryEntity
import com.lasec.monitoreoapp.data.database.entities.incident_entities.toSubCategoryEntity
import com.lasec.monitoreoapp.data.repository.incident_reports_repository.CategoriesRepository
import com.lasec.monitoreoapp.data.repository.incident_reports_repository.SubCategoriesRepository
import com.lasec.monitoreoapp.domain.model.incident_reports_model.Categories
import javax.inject.Inject

class SyncCategoriesAndSubCategoriesUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository,
    private val subCategoriesRepository: SubCategoriesRepository
) {
    suspend operator fun invoke(categories: List<Categories>) {
        // Insertar categorías
        val categoryEntities = categories.map { it.toCategoryEntity() }
        categoriesRepository.upsertCategoriesToDatabase(categoryEntities)

        // Insertar subcategorías
        val subCategoryEntities = categories.flatMap { category ->
            category.subCategories.map { it.toSubCategoryEntity() }
        }
        subCategoriesRepository.upsertSubCategoriesToDatabase(subCategoryEntities)
    }
}