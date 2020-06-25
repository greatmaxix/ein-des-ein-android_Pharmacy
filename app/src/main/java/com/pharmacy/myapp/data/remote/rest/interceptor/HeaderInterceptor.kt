package com.pharmacy.myapp.data.remote.rest.interceptor

import com.pharmacy.myapp.data.local.SPManager
import okhttp3.Interceptor
import okhttp3.Response
import org.koin.core.KoinComponent
import org.koin.core.inject

class HeaderInterceptor : Interceptor, KoinComponent {

    private val repository: SPManager by inject()

    override fun intercept(chain: Interceptor.Chain): Response {
        val request = chain.request()
        val newRequestBuilder = request.newBuilder()
        with(newRequestBuilder) {
            header(CONTENT_TYPE, APPLICATION_JSON)
            val token = repository.token ?: ""
            if (token.isNotBlank()) {
                header(AUTH_TOKEN, "$BEARER $token")
            }
        }

        return chain.proceed(newRequestBuilder.build())
    }

    companion object {

        private const val AUTH_TOKEN = "Authorization"
        private const val BEARER = "Bearer:"
        private const val CONTENT_TYPE = "Content-Type"
        private const val APPLICATION_JSON = "application/json"
    }
}