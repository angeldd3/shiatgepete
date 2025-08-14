package com.lasec.monitoreoapp.domain.model

import com.google.gson.annotations.SerializedName
import com.lasec.monitoreoapp.domain.model.manual_workorders.ActivityTypeDispatchVehicleType


data class Activities (
    @SerializedName("activityTypeId") val activityTypeId: Int,
    @SerializedName("name") val name: String,
    @SerializedName("activityTypeDispatchVehicleTypes") val activityTypeDispatchVehicleTypes: List<ActivityTypeDispatchVehicleType>

)


data class ActivityOption(
    val activityTypeId: Int,
    val activityName: String
)