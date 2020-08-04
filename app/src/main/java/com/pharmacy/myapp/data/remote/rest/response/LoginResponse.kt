package com.pharmacy.myapp.data.remote.rest.response

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @SerializedName("customer") val customer: Customer,
    @SerializedName("refresh_token") val refreshToken: String,
    @SerializedName("token") val token: String
)

data class Customer(
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("username") val name: String,
    @SerializedName("uuid") val uuid: String,
    @SerializedName("avatarUuid") val avatarUuid: String
)