package com.pharmacy.myapp.data.remote.rest.request.order

import com.google.gson.annotations.SerializedName

data class ProductOrderData(
    @SerializedName("pharmacyProductId") private val pharmacyProductId : Int,
    @SerializedName("requestedPrice") private val requestedPrice : Double,
    @SerializedName("count") private val count : Int,
)