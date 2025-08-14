package com.lasec.monitoreoapp.data.repository.incident_reports_repository

import com.lasec.monitoreoapp.data.database.dao.incident_reports_dao.CategoriesDao
import com.lasec.monitoreoapp.data.database.entities.incident_entities.CategoriesEntity
import com.lasec.monitoreoapp.data.remote.RetrofitInstance
import com.lasec.monitoreoapp.domain.model.incident_reports_model.Categories
import javax.inject.Inject

class CategoriesRepository @Inject constructor(private val categoriesDao: CategoriesDao) {

    suspend fun getAllCategoriesFromApi(): List<Categories>{
        return RetrofitInstance.categoriesApi.getAllCategories()
    }

    suspend fun upsertCategoriesToDatabase(categories: List<CategoriesEntity>){
            categoriesDao.upsertCategories(categories)
    }

    suspend fun getAllCategoriesFromDatabase(): List<CategoriesEntity>{
        return categoriesDao.getAllCategories()
    }

    suspend fun getAllCategoriesFromDatabaseByActivityId(activityId: Int): List<CategoriesEntity>{
        return categoriesDao.getAllCategoriesByActivityId(activityId)
    }
}