package com.lasec.monitoreoapp.data.remote

import com.lasec.monitoreoapp.domain.model.Activities
import retrofit2.http.GET
import retrofit2.http.Path

interface ActivitiesApiService{
    @GET("service/dispatch/api/v1/ActivityTypes/{activityTypeId}")
    suspend fun getActivitiesById(
        @Path("activityTypeId") activityTypeId: Int
    ): Activities

    @GET("service/dispatch/api/v1/ActivityTypes")
    suspend fun getAllActivities():List<Activities>
}



