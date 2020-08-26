package com.pharmacy.myapp.product.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Picture(
    @SerializedName("url") val url: String,
    @SerializedName("uuid") val uuid: String
) : Parcelable