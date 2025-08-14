package com.lasec.monitoreoapp.domain.model

import com.google.gson.annotations.SerializedName

data class Vehicles(
    @SerializedName("indexVehicle") val indexVehicle: IndexVehicle,
    @SerializedName("vehicle") val vehicleInfo: VehicleInfo
)

data class IndexVehicle(
    @SerializedName("indexVehicleId") val indexVehicleId: Int
)

data class VehicleInfo(
    @SerializedName("economicNumber") val economicNumber: String,
    @SerializedName("vehicleTypeId") val vehicleTypeId: Int
)
