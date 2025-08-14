package com.lasec.monitoreoapp.domain.model.manual_workorders

data class RegisterActivityParams(
    val indexEmployeeId: Int,
    val workShiftId: Int,
    val workShiftDescription: String,
    val workShiftStart: String, // "HH:mm[:ss]"
    val workShiftEnd: String,   // "HH:mm[:ss]"
    val indexVehicleId: Int,
    val economicNumber: String,
    val vehicleTypeId: Int,
    val activityTypeId: Int,
    val activityName: String,
    val quantity: Double,
    val placeId: Int,
    val placeName: String,
    val initTimeLocal: String, // "HH:mm[:ss]" elegido en UI
    val endTimeLocal: String   // "HH:mm[:ss]"
)
