package com.pharmacy.myapp.product.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Aggregation(
    @SerializedName("maxPrice") val maxPrice: Double,
    @SerializedName("minPrice") val minPrice: Double
) : Parcelable