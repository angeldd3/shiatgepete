package com.lasec.monitoreoapp.domain.model.incident_reports_model

import com.google.gson.annotations.SerializedName
import com.lasec.monitoreoapp.data.database.entities.incident_entities.SubCategoriesEntity

data class SubCategories(
    @SerializedName("SubCategoryId") val subCategoryId: Int,
    @SerializedName("Name") val name: String,
    @SerializedName("CategoryId") val categoryId: Int
)
//Modelo de dominio
data class SubCategory(
    val subCategoryId: Int,
    val name: String,
    val categoryId: Int
)

fun SubCategoriesEntity.toDomain(): SubCategory {
    return SubCategory(
        subCategoryId = this.subCategoryId,
        name = this.name,
        categoryId = this.categoryId
    )
}