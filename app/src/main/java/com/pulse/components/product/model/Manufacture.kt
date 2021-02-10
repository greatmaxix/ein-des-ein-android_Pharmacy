package com.pulse.components.product.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class Manufacture(
    @SerializedName("localName") val producer: String = "",
    @SerializedName("iso3CountryCode") val isoCode: String = ""
) : Parcelable