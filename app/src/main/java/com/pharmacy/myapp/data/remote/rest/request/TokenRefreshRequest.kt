package com.pharmacy.myapp.data.remote.rest.request

import com.google.gson.annotations.SerializedName

data class TokenRefreshRequest(
    @SerializedName("refresh_token") val refreshToken: String
)