package com.lasec.monitoreoapp.data.remote

import okhttp3.Interceptor
import okhttp3.Response

class CookieInterceptor : Interceptor {
    override fun intercept(chain: Interceptor.Chain): Response {
        val requestWithCookies = chain.request().newBuilder()
            .addHeader(
                "Cookie",
                ""
            )
            .build()
        return chain.proceed(requestWithCookies)
    }
}
