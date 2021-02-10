package com.pulse.components.user.model.avatar

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class Avatar(
    @ColumnInfo(name = "createdAtAvatar")
    @SerializedName("createdAt") val createdAt: String?,
    @SerializedName("extension") val extension: String?,
    @SerializedName("filename") val filename: String?,
    @SerializedName("mimeType") val mimeType: String?,
    @SerializedName("originalFilename") val originalFilename: String?,
    @SerializedName("ownerUuid") val ownerUuid: String?,
    @SerializedName("type") val type: String?,
    @SerializedName("url") val url: String,
    @PrimaryKey
    @ColumnInfo(name = "uuidAvatar")
    @SerializedName("uuid") val uuid: String
)