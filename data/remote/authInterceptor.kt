package com.lasec.monitoreoapp.data.remote

import com.lasec.monitoreoapp.auth.TokenManager
import okhttp3.Interceptor
import okhttp3.Response




class AuthInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val token = TokenManager.getAccessToken()

        val request = chain.request().newBuilder()
            .addHeader("Authorization", "Bearer $token")
            .build()

        return chain.proceed(request)
    }
}



