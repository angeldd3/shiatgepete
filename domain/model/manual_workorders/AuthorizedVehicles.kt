package com.lasec.monitoreoapp.domain.model.manual_workorders

import com.google.gson.annotations.SerializedName

data class AuthorizedVehicles(
    @SerializedName("authorizedVehicleId") val authorizedVehicleId: String,
    @SerializedName("indexVehicleId") val indexVehicleId: Int
)


data class AuthorizedVehiclesDto(
    val workOrderId: String,
    val indexVehicleId: Int,
    private val stepWizard: Int  = 1
)