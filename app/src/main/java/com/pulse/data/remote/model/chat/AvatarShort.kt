package com.pulse.data.remote.model.chat

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AvatarShort(@SerializedName("url") val url: String, @SerializedName("uuid") val uuid: String) : Parcelable