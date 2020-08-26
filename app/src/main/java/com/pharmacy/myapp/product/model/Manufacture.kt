package com.pharmacy.myapp.product.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Manufacture(
    @SerializedName("localName") val producer: String = "",
    @SerializedName("countryRusName") val producerRu: String = ""
) : Parcelable