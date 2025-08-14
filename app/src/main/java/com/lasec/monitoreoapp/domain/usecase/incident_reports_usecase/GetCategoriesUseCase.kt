package com.lasec.monitoreoapp.domain.usecase.incident_reports_usecase


import com.lasec.monitoreoapp.data.repository.incident_reports_repository.CategoriesRepository
import com.lasec.monitoreoapp.domain.model.incident_reports_model.Category
import com.lasec.monitoreoapp.domain.model.incident_reports_model.toDomain
import javax.inject.Inject

class GetCategoriesUseCase @Inject constructor(
    private val categoriesRepository: CategoriesRepository
) {
    suspend operator fun invoke(): List<Category> {
        val response = categoriesRepository.getAllCategoriesFromDatabase()
        return response.map{it.toDomain()}
    }
}

//eesto va en el viewmodel
//viewModelScope.launch {
//    val apiCategories = RetrofitInstance.categoriesApi.getAllCategories()
//    syncCategoriesAndSubCategoriesUseCase(apiCategories)
//
//    val categoryId = 5
//    val subcategories = getSubCategoriesByCategoryIdUseCase(categoryId)
//    _subcategoriesLiveData.postValue(subcategories)