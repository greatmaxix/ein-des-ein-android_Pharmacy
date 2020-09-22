package com.pharmacy.myapp.cart.model

import com.google.gson.annotations.SerializedName

data class CartProductInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("count") var count: Int
)