package com.pulse.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Picture(@SerializedName("url") val url: String) : Parcelable