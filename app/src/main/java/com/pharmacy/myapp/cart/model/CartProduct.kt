package com.pharmacy.myapp.cart.model

import com.google.gson.annotations.SerializedName
import com.pharmacy.myapp.App
import com.pharmacy.myapp.model.Picture
import com.pharmacy.myapp.product.model.Manufacture
import java.util.*

data class CartProduct(
    @SerializedName("pharmacyProductId") val productId: Int,
    @SerializedName("rusName") val rusName: String,
    @SerializedName("releaseForm") val releaseForm: String,
    @SerializedName("pictures") val pictures: List<Picture>,
    @SerializedName("productCartPharmacyProduct") val cartProductInfo: CartProductInfo,
    @SerializedName("manufacturerData") val manufacture: Manufacture,
    @SerializedName("price") val price: Double
) {
    //TODO Create global "Local helper"
    val productLocale: String?
        get() = App.localeMap[manufacture.isoCode]?.getDisplayCountry(Locale("RU"))
}