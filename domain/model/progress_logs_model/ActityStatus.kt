package com.lasec.monitoreoapp.domain.model.progress_logs_model

import com.google.gson.annotations.SerializedName
import com.lasec.monitoreoapp.data.database.entities.progress_logs_entities.ActivityStatusEntity

data class ActivityStatus (
    @SerializedName("activityStatusId") val activityStatusId: Int,
    @SerializedName("name") val name: String
)

data class ActivityStatusDomain(
    val id: Int,
    val name: String
)

fun ActivityStatusEntity.toDomain() = ActivityStatusDomain(
    id = activityStatusId,
    name = name
)