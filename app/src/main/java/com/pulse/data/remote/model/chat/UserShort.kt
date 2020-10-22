package com.pulse.data.remote.model.chat

import android.os.Parcelable
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserShort(
    @SerializedName("id") val id: Int,
    @SerializedName("uuid") val uuid: String,
    @SerializedName("type") val type: String,
    @SerializedName("name") val name: String,
    @Embedded(prefix = "avatar")
    @SerializedName("avatar") val avatar: AvatarShort?
) : Parcelable