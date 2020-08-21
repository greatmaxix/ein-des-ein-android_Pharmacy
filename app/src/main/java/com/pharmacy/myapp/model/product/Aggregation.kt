package com.pharmacy.myapp.model.product

import com.google.gson.annotations.SerializedName

data class Aggregation(
    @SerializedName("maxPrice") val maxPrice: Double,
    @SerializedName("minPrice") val minPrice: Double
)