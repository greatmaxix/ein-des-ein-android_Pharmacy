package com.pharmacy.myapp.data.remote.rest.response

import com.google.gson.annotations.SerializedName

data class UserDataResponse(
    @SerializedName("id") val id: String,
    @SerializedName("username") val username: String,
    @SerializedName("email") val email: String,
    @SerializedName("uuid") val uuid: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("phone") val phone: String
)