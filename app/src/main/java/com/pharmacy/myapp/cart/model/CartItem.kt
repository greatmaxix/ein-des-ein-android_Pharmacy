package com.pharmacy.myapp.cart.model

import com.google.gson.annotations.SerializedName
import com.pharmacy.myapp.model.Location
import com.pharmacy.myapp.model.Logo

data class CartItem(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("location") val location: Location,
    @SerializedName("logo") val logo: Logo,
    @SerializedName("pharmacyProducts") val products: MutableList<CartProduct>
) {
    val totalPrice
        get() = products.sumByDouble { it.price }

    fun updateCount(count: Int, position: Int) {
        products[position].cartProductInfo.count = count
    }
}