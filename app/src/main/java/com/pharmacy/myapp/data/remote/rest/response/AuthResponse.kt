package com.pharmacy.myapp.data.remote.rest.response

import com.google.gson.annotations.SerializedName
import com.pharmacy.myapp.user.model.customerInfo.Customer

data class AuthResponse(
    @SerializedName("item") val customer: Customer,
    @SerializedName("refreshToken") val refreshToken: String,
    @SerializedName("token") val token: String
)