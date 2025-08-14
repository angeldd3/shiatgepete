package com.lasec.monitoreoapp.data.database.dao.incident_reports_dao

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
import com.lasec.monitoreoapp.data.database.entities.incident_entities.CategoriesEntity

@Dao
interface CategoriesDao {

    @Query("SELECT * FROM categories_table ORDER BY name")
    suspend fun getAllCategories(): List<CategoriesEntity>


    @Query("SELECT * FROM categories_table WHERE activity_type_id = :activityId")
    suspend fun getAllCategoriesByActivityId(activityId: Int): List<CategoriesEntity>

    @Upsert
    suspend fun upsertCategories(categories: List<CategoriesEntity>)
}