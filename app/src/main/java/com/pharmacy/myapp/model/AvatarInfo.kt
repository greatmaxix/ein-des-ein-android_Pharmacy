package com.pharmacy.myapp.model

import com.google.gson.annotations.SerializedName

data class AvatarInfo(
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("extension") val extension: String,
    @SerializedName("filename") val filename: String,
    @SerializedName("mimeType") val mimeType: String,
    @SerializedName("originalFilename") val originalFilename: String,
    @SerializedName("ownerUuid") val ownerUuid: String,
    @SerializedName("type") val type: String,
    @SerializedName("url") val url: String,
    @SerializedName("uuid") val uuid: String
)