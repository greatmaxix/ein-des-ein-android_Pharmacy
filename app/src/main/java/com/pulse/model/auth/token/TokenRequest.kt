package com.pulse.model.auth.token

import com.google.gson.annotations.SerializedName

data class TokenRequest(
    @SerializedName("refresh_token") val refreshToken: String
)