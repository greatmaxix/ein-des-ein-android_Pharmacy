package com.pharmacy.myapp.cart.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.pharmacy.myapp.model.Location
import com.pharmacy.myapp.model.Logo
import kotlinx.android.parcel.Parcelize
import java.math.BigDecimal
import java.math.RoundingMode

@Parcelize
data class CartItem(
    @SerializedName("id") val id: Int,
    @SerializedName("name") val name: String,
    @SerializedName("location") val location: Location,
    @SerializedName("logo") val logo: Logo,
    @SerializedName("pharmacyProducts") val products: MutableList<CartProduct>
) : Parcelable {
    val totalPrice: BigDecimal
        get() = products.sumOf { it.price.toBigDecimal().multiply(it.cartProductInfo.count.toBigDecimal()) }.setScale(2, RoundingMode.DOWN)

    fun updateCount(count: Int, position: Int) {
        products[position].cartProductInfo.count = count
    }

    val productOrderList
        get() = products.map { it.productOrderData }
}