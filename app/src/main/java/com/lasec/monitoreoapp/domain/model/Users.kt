package com.lasec.monitoreoapp.domain.model

import com.google.gson.annotations.SerializedName

data class Users(
    @SerializedName("Id") val id: String,
    @SerializedName("FullName") val fullName: String
)


