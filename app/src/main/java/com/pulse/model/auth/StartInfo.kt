package com.pulse.model.auth

import com.google.gson.annotations.SerializedName
import com.pulse.components.user.model.customer.Customer

data class StartInfo(
    @SerializedName("item") val customer: Customer,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("token") val token: String
)