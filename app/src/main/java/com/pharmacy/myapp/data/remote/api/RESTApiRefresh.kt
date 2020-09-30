package com.pharmacy.myapp.data.remote.api

import com.pharmacy.myapp.model.auth.token.TokenRequest
import com.pharmacy.myapp.model.auth.token.TokenModel
import retrofit2.http.Body
import retrofit2.http.POST

interface RESTApiRefresh {

    @POST("/api/v1/customer/refresh_token")
    fun tokenRefresh(@Body body: TokenRequest): TokenModel

}