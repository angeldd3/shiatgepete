package com.lasec.monitoreoapp.domain.model.manual_workorders

import com.google.gson.annotations.SerializedName

class DispatchVehicleTypeIndexEmployees(
    @SerializedName("dispatchVehicleTypeIndexEmployeeId") val dispatchVehicleTypeIndexEmployeeId: Int,
    @SerializedName("dispatchVehicleTypeId") val dispatchVehicleTypeId: Int,
    @SerializedName("indexEmployeeId") val indexEmployeeId: Int
)


data class VehicleOption(
    val indexVehicleId: Int,
    val economic_number: String,
    val vehicleTypeId: Int
)
