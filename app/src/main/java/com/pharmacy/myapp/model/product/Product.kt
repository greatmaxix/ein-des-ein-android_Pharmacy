package com.pharmacy.myapp.model.product

import com.google.gson.annotations.SerializedName
import com.pharmacy.myapp.model.Picture

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
)