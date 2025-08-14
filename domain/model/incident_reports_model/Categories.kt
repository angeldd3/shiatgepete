package com.lasec.monitoreoapp.domain.model.incident_reports_model

import com.google.gson.annotations.SerializedName
import com.lasec.monitoreoapp.data.database.entities.incident_entities.CategoriesEntity

data class Categories(
    @SerializedName("CategoryId") val categoryId: Int,
    @SerializedName("Name") val name: String,
    @SerializedName("ActivityTypeId") val activityTypeId: Int,
    @SerializedName("SubCategories") val subCategories: List<SubCategories>
)

//Model de dominio
data class Category(
    val categoryId: Int,
    val name: String,
    val activityTypeId: Int
)

//Combierte de categoriesEntity al modelo de dominio
fun CategoriesEntity.toDomain(): Category {
    return Category(
        categoryId = this.categoryId,
        name = this.name,
        activityTypeId = this.activityTypeId
    )
}