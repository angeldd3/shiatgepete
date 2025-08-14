package com.lasec.monitoreoapp.data.remote.dto

import com.google.gson.annotations.SerializedName

data class TokenResponse(
    @SerializedName("access_token")
    val accessToken: String,

    @SerializedName("expires_in")
    val expiresIn: Long,

    @SerializedName("token_type")
    val tokenType: String,

    @SerializedName("refresh_token")
    val refreshToken: String
)
