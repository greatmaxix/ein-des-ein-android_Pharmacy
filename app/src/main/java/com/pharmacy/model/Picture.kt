package com.pharmacy.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Picture(@SerializedName("url") val url: String) : Parcelable