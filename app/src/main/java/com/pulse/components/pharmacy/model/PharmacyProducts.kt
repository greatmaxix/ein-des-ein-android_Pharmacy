package com.pulse.components.pharmacy.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PharmacyProducts(@SerializedName("pharmacyProductId") val pharmacyProductId: Int, @SerializedName("price") val price: Float) : Parcelable