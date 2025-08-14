package com.lasec.monitoreoapp.data.database.entities.incident_entities

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.lasec.monitoreoapp.domain.model.incident_reports_model.SubCategories

@Entity(
    tableName = "subcategories_table",
    foreignKeys = [ForeignKey(
        entity = CategoriesEntity::class,
        parentColumns = ["category_id"],
        childColumns = ["category_id"],
        onDelete = ForeignKey.CASCADE
    )],
    indices = [Index(value = ["category_id"])]
)
data class SubCategoriesEntity(
    @PrimaryKey @ColumnInfo(name = "sub_category_id") val subCategoryId: Int,
    @ColumnInfo(name = "name") val name: String,
    @ColumnInfo(name = "category_id") val categoryId: Int
)

fun SubCategories.toSubCategoryEntity() = SubCategoriesEntity(
    subCategoryId = subCategoryId,
    name = name,
    categoryId = categoryId
)