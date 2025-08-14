package com.lasec.monitoreoapp.data.remote

import com.lasec.monitoreoapp.data.remote.dto.TokenResponse
import retrofit2.http.*

interface AuthApiService {

    @FormUrlEncoded
    @POST("api/openid/connect/token")
    suspend fun loginWithPassword(
        @Field("grant_type") grantType: String = "password",
        @Field("username") username: String = "root",
        @Field("password") password: String = "lvCYxDmU^xlxU0T",
        @Field("scope") scope: String = "appSmartflow IdentityServerApi offline_access",
        @Field("client_id") clientId: String = "private.app.droid",
        @Field("client_secret") clientSecret: String = "\$eg^Bv2!8wLr8Fu*BGXjyRBVZ2L83x"
    ): TokenResponse

    @FormUrlEncoded
    @POST("api/openid/connect/token")
    suspend fun refreshToken(
        @Field("grant_type") grantType: String = "refresh_token",
        @Field("refresh_token") refreshToken: String,
        @Field("client_id") clientId: String = "private.app.droid",
        @Field("client_secret") clientSecret: String = "\$eg^Bv2!8wLr8Fu*BGXjyRBVZ2L83x"
    ): TokenResponse
}



