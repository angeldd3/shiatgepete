package com.lasec.monitoreoapp.data.remote

import android.util.Log
import com.lasec.monitoreoapp.auth.TokenManager
import kotlinx.coroutines.runBlocking
import okhttp3.Authenticator
import okhttp3.Request
import okhttp3.Response
import okhttp3.Route



class TokenAuthenticator : Authenticator {
    override fun authenticate(route: Route?, response: Response): Request? {
        val refreshToken = TokenManager.getRefreshToken()

        // ✅ Evita intento de refresh si ya lo hiciste antes
        if (response.request().header("Authorization-Attempted") != null) {
            return null
        }

        if (refreshToken.isNullOrEmpty()) return null

        return runBlocking {
            try {
                val newTokenResponse = RetrofitInstance.authApi.refreshToken(
                    grantType = "refresh_token",
                    refreshToken = refreshToken
                )

                TokenManager.saveTokens(
                    newTokenResponse.accessToken,
                    newTokenResponse.refreshToken,
                    newTokenResponse.expiresIn
                )

                response.request()
                    .newBuilder()
                    .header("Authorization", "Bearer ${newTokenResponse.accessToken}")
                    .header("Authorization-Attempted", "true") // marca para evitar bucles
                    .build()

            } catch (e: Exception) {
                null
            }
        }
    }
}

