package com.lasec.monitoreoapp.data.database.entities.incident_entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.lasec.monitoreoapp.domain.model.incident_reports_model.Categories

@Entity(tableName = "categories_table")
data class CategoriesEntity(
    @PrimaryKey @ColumnInfo(name = "category_id") val categoryId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "activity_type_id") val activityTypeId: Int
)

fun Categories.toCategoryEntity() = CategoriesEntity(
    categoryId = categoryId,
    name = name,
    activityTypeId = activityTypeId
)