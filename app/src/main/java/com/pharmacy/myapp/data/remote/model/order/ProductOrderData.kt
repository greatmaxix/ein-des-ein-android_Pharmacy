package com.pharmacy.myapp.data.remote.model.order

import com.google.gson.annotations.SerializedName

data class ProductOrderData(
    @SerializedName("pharmacyProductId") private val pharmacyProductId : Int,
    @SerializedName("price") private val price : Double,
    @SerializedName("count") private val count : Int,
)