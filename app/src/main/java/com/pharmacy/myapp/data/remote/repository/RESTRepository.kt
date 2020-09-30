package com.pharmacy.myapp.data.remote.repository

import com.pharmacy.myapp.core.network.ResponseWrapper
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.api.RESTApiRefresh
import com.pharmacy.myapp.model.auth.token.TokenRequest

class RESTRepository(private val sp: SPManager, private val APIRefresh: RESTApiRefresh) {

    fun refreshToken() {
        sp.refreshToken?.let {
            sp.saveTokens(APIRefresh.tokenRefresh(TokenRequest(it)))
            ResponseWrapper.Success(Unit)
        } ?: throw IllegalArgumentException("refreshToken is empty")
    }

}