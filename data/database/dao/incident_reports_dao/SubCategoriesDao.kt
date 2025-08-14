package com.lasec.monitoreoapp.data.database.dao.incident_reports_dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lasec.monitoreoapp.data.database.entities.incident_entities.SubCategoriesEntity

@Dao
interface SubCategoriesDao {
    @Query("SELECT * FROM subcategories_table ORDER BY name")
    suspend fun getAllSubCategories(): List<SubCategoriesEntity>

    @Query("SELECT * FROM subcategories_table WHERE category_id = :categoryId")
    suspend fun getSubcategoriesByCategory(categoryId: Int): List<SubCategoriesEntity>

    @Upsert
    suspend fun upsertSubCategories(subCategories: List<SubCategoriesEntity>)
}