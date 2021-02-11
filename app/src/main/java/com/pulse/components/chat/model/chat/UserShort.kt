package com.pulse.components.chat.model.chat

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class UserShort(
    @ColumnInfo(name = "userId")
    @SerializedName("id") val id: Int,
    @SerializedName("uuid") val uuid: String,
    @SerializedName("type") val type: String,
    @ColumnInfo(name = "customerName")
    @SerializedName("name") val name: String,
    @Embedded(prefix = "avatar")
    @SerializedName("avatar") val avatar: AvatarShort?
) : Parcelable