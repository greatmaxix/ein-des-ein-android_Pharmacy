package com.pharmacy.myapp.model

import com.google.gson.annotations.SerializedName

data class Product(
    @SerializedName("globalProductId") val globalProductId: Int,
    @SerializedName("engName") val engName: String,
    @SerializedName("rusName") val rusName: String,
    @SerializedName("description") val description: String,
    @SerializedName("maxPrice") val maxPrice: Double,
    @SerializedName("minPrice") val minPrice: Double,
    @SerializedName("barCodes") val barCodes: List<String>,
    @SerializedName("pictures") val pictures: List<Picture>,
    @SerializedName("releaseForm") val releaseForm: String,
    @SerializedName("registrationDateAndNumber") val registrationDateAndNumber: String
)