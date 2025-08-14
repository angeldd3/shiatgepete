package com.lasec.monitoreoapp.domain.model.manual_workorders

import com.google.gson.annotations.SerializedName

data class DispatchVehicleTypes(
    @SerializedName("dispatchVehicleTypeId") val dispatchVehicleTypeId: Int,
    @SerializedName("vehicleTypeId") val vehicleTypeId: Int
)