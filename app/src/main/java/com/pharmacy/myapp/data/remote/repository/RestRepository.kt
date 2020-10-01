package com.pharmacy.myapp.data.remote.repository

import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.api.RestApiRefresh
import com.pharmacy.myapp.model.auth.token.TokenRequest

class RestRepository(private val sp: SPManager, private val api: RestApiRefresh) {

    fun refreshToken() = sp.refreshToken?.let {
        val newTokens = TokenRequest(it)
        sp.saveTokens(api.tokenRefresh(newTokens))
        sp.token
    } ?: throw IllegalArgumentException("refreshToken is empty")
}