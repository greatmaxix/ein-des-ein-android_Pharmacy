package com.pharmacy.myapp.data.remote.interceptor

import com.pharmacy.myapp.data.local.SPManager
import okhttp3.Interceptor
import okhttp3.Response
import timber.log.Timber

class RESTHeaderInterceptor(private val sp: SPManager) : Interceptor {

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