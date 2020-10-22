package com.pulse.components.cart.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.pulse.model.Location
import com.pulse.model.Logo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartItem(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("location") val location: Location,
    @SerializedName("logo") val logo: Logo,
    @SerializedName("pharmacyProducts") val products: MutableList<CartProduct>
) : Parcelable {

    val totalPrice
        get() = products.sumByDouble { it.price * it.count }

    val totalCount
        get() = products.sumBy { it.count }

    val productOrderList
        get() = products.map { it.productOrderData }

    fun updateCount(count: Int, position: Int) {
        products[position].count = count
    }
}