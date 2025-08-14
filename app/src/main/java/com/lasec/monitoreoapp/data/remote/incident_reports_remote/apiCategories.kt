package com.lasec.monitoreoapp.data.remote.incident_reports_remote

import com.lasec.monitoreoapp.domain.model.incident_reports_model.Categories
import retrofit2.http.GET

interface CategoriesApiService {
    @GET("service/persistent/api/v1/categories/all")
    suspend fun getAllCategories(): List<Categories>

}