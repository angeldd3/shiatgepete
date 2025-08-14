package com.lasec.monitoreoapp.domain.model.manual_workorders

import com.google.gson.annotations.SerializedName

data class ActivityTypeDispatchVehicleType(
    @SerializedName("activityTypeDispatchVehicleTypeId") val activityTypeDispatchVehicleTypeId: Int,
    @SerializedName("activityTypeId") val activityTypeId: Int,
    @SerializedName("dispatchVehicleTypeId") val dispatchVehicleTypeId:Int
)