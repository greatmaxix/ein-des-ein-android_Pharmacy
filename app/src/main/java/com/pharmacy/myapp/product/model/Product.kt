package com.pharmacy.myapp.product.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Product(
    @SerializedName("globalProductId") val globalProductId: Int,
    @SerializedName("engName") val engName: String,
    @SerializedName("rusName") val rusName: String,
    @SerializedName("pharmacyProductsAggregationData") val aggregation: Aggregation,
    @SerializedName("description") val description: String,
    @SerializedName("barCodes") val barCodes: List<String>,
    @SerializedName("pictures") val pictures: List<Picture>,
    @SerializedName("releaseForm") val releaseForm: String,
    @SerializedName("registrationDateAndNumber") val registrationDateAndNumber: String
) : Parcelable