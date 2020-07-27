package com.pharmacy.myapp.model

import com.google.gson.annotations.SerializedName

data class CustomerResponse(
    @SerializedName("avatar") val avatarInfo: AvatarInfo,
    @SerializedName("avatarUuid") val avatarUuid: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("email") val email: String,
    @SerializedName("id") val id: Int,
    @SerializedName("phone") val phone: String,
    @SerializedName("username") val name: String,
    @SerializedName("uuid") val uuid: String
)