package com.pharmacy.product.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Aggregation(
    @SerializedName("maxPrice") val maxPrice: Double = 0.00,
    @SerializedName("minPrice") val minPrice: Double = 0.00
) : Parcelable