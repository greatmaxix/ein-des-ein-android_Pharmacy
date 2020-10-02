package com.pharmacy.myapp.cart.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.pharmacy.myapp.App
import com.pharmacy.myapp.orders.model.ProductOrderData
import com.pharmacy.myapp.model.Picture
import com.pharmacy.myapp.product.model.Manufacture
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class CartProduct(
    @SerializedName("pharmacyProductId") val productId: Int,
    @SerializedName("rusName") val rusName: String,
    @SerializedName("releaseForm") val releaseForm: String,
    @SerializedName("pictures") val pictures: List<Picture>,
    @SerializedName("productCartPharmacyProduct") val cartProductInfo: CartProductInfo,
    @SerializedName("manufacturerData") val manufacture: Manufacture,
    @SerializedName("price") val price: Double
) : Parcelable {
    //TODO Create global "Local helper"
    val productLocale: String?
        get() = App.localeMap[manufacture.isoCode]?.getDisplayCountry(Locale("RU"))

    val productOrderData
        get() = ProductOrderData(productId, price, cartProductInfo.count)

    val firstPictureUrl
        get() = pictures.firstOrNull()?.url
}