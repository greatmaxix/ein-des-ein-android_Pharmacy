package com.pharmacy.myapp.data.remote.rest.response

import com.google.gson.annotations.SerializedName
import com.pharmacy.myapp.model.customerInfo.CustomerInfo

data class AuthResponse(
    @SerializedName("item") val customer: CustomerInfo,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("token") val token: String
)