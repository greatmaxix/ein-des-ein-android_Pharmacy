package com.pharmacy.model.auth.token

import com.google.gson.annotations.SerializedName

data class TokenModel(
    @SerializedName("token") val token: String,
    @SerializedName("refresh_token") val refreshToken: String
)