package com.pharmacy.myapp.cart.model

import com.google.gson.annotations.SerializedName
import com.pharmacy.myapp.model.Location
import com.pharmacy.myapp.model.Logo

data class CartItem(
    val id: Int,
    val name: String,
    val location: Location,
    val logo: Logo,
    @SerializedName("pharmacyProducts") val products: MutableList<CartProduct>
) {
    val totalPrice
        get() = products.sumByDouble { it.price }

    fun updateCount(count: Int, position: Int) {
        products[position].cartProductInfo.count = count
    }
}