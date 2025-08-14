package com.lasec.monitoreoapp.data.remote

import android.content.Context
import com.lasec.monitoreoapp.auth.TokenManager

object TokenProvider {

    suspend fun initClientToken(context: Context) {
        val tokenResponse = RetrofitInstance.authApi.loginWithPassword()
        TokenManager.saveTokens(tokenResponse.accessToken, tokenResponse.refreshToken, tokenResponse.expiresIn)

        // Si usas DataStore:
//        DataStoreManager(context).saveAccessToken(tokenResponse.accessToken)
//        DataStoreManager(context).saveRefreshToken(tokenResponse.refreshToken)
    }
}
