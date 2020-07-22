package com.pharmacy.myapp.data.remote.rest.response

import com.google.gson.annotations.SerializedName

data class TokenRefreshResponse(
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("token") val token: String
)