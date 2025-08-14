package com.lasec.monitoreoapp.auth

object TokenManager {
    private var accessToken: String? = null
    private var refreshToken: String? = null

    private var expiresAt: Long = 0L // Tiempo en milisegundos

    fun saveTokens(access: String, refresh: String, expiresIn: Long) {
        accessToken = access
        refreshToken = refresh
        expiresAt = System.currentTimeMillis() + (expiresIn * 1000)
    }

    fun getAccessToken(): String {
        return accessToken ?: ""
    }

    fun getRefreshToken(): String {
        return refreshToken ?: ""
    }

    fun isTokenExpired(): Boolean {
        return System.currentTimeMillis() >= expiresAt
    }

    fun clearTokens() {
        accessToken = null
        refreshToken = null
    }
}
