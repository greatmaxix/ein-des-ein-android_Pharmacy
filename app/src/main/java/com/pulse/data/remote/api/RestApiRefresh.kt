package com.pulse.data.remote.api

import com.pulse.model.auth.token.TokenModel
import com.pulse.model.auth.token.TokenRequest
import retrofit2.http.Body
import retrofit2.http.POST

interface RestApiRefresh {

    @POST("/api/v1/customer/refresh_token")
    fun tokenRefresh(@Body body: TokenRequest): TokenModel

}