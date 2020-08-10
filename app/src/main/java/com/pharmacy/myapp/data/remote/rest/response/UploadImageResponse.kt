package com.pharmacy.myapp.data.remote.rest.response

import com.google.gson.annotations.SerializedName

data class UploadImageResponse(@SerializedName("item") val avatar: Avatar)

data class Avatar(
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