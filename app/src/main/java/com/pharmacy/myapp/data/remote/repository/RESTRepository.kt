package com.pharmacy.myapp.data.remote.repository

import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.api.RESTApiRefresh
import com.pharmacy.myapp.model.auth.token.TokenRequest

class RESTRepository(private val sp: SPManager, private val APIRefresh: RESTApiRefresh) {

    fun refreshToken() = sp.refreshToken?.let {
        val newTokens = TokenRequest(it)
        sp.saveTokens(APIRefresh.tokenRefresh(newTokens))
        sp.token
    } ?: throw IllegalArgumentException("refreshToken is empty")
}