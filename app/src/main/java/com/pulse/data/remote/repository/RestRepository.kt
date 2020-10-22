package com.pulse.data.remote.repository

import com.pulse.data.local.SPManager
import com.pulse.data.remote.api.RestApiRefresh
import com.pulse.model.auth.token.TokenRequest

class RestRepository(private val sp: SPManager, private val api: RestApiRefresh) {

    fun refreshToken() = sp.refreshToken?.let {
        val newTokens = TokenRequest(it)
        sp.saveTokens(api.tokenRefresh(newTokens))
        sp.token
    } ?: throw IllegalArgumentException("refreshToken is empty")
}