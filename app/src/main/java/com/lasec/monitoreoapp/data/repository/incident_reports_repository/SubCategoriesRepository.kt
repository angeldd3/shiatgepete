package com.lasec.monitoreoapp.data.repository.incident_reports_repository

import com.lasec.monitoreoapp.data.database.dao.incident_reports_dao.SubCategoriesDao
import com.lasec.monitoreoapp.data.database.entities.incident_entities.SubCategoriesEntity
import javax.inject.Inject

class SubCategoriesRepository @Inject constructor(
    private val subCategoriesDao: SubCategoriesDao
) {


    suspend fun getSubCategoriesByCategoryId(categoryId: Int): List<SubCategoriesEntity> {
        return subCategoriesDao.getSubcategoriesByCategory(categoryId)
    }


    suspend fun upsertSubCategoriesToDatabase(subcategories: List<SubCategoriesEntity>) {
        subCategoriesDao.upsertSubCategories(subcategories)
    }
}