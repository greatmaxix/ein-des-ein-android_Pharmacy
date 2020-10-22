package com.pulse.data.remote.interceptor

import com.pulse.data.local.SPManager
import okhttp3.Interceptor
import okhttp3.Response

class RestHeaderInterceptor(private val sp: SPManager) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val original = chain.request()
        val token = sp.token

        return if (!token.isNullOrEmpty()) {
            chain.proceed(original.newBuilder().header("Authorization", "Bearer $token").build())
        } else {
            chain.proceed(original)
        }

    }
}