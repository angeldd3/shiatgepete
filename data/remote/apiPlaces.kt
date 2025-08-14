package com.lasec.monitoreoapp.data.remote

import com.lasec.monitoreoapp.domain.model.Places
import retrofit2.http.GET
import retrofit2.http.Path

interface PlacesApiService{
    @GET("service/catalog/api/v1/Places/{placeId}")
    suspend fun getPlaceById(
        @Path("placeId")  placeId: Int
    ): Places

    @GET("service/catalog/api/v1/Places/all")
    suspend fun getAllPlaces(): List<Places>
}