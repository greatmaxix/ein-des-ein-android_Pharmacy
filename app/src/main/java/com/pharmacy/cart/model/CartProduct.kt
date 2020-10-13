package com.pharmacy.cart.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.pharmacy.App
import com.pharmacy.data.remote.model.order.ProductOrderData
import com.pharmacy.model.Picture
import com.pharmacy.product.model.Manufacture
import kotlinx.android.parcel.Parcelize
import java.util.*

@Parcelize
data class CartProduct(
    @SerializedName("pharmacyProductId") val productId: Int,
    @SerializedName("rusName") val rusName: String,
    @SerializedName("releaseForm") val releaseForm: String,
    @SerializedName("pictures") val pictures: List<Picture>,
    @SerializedName("manufacturerData") val manufacture: Manufacture,
    @SerializedName("price") val price: Double,
    @SerializedName("productCount") var count: Int
) : Parcelable {
    //TODO Create global "Local helper"
    val productLocale: String?
        get() = App.localeMap[manufacture.isoCode]?.getDisplayCountry(Locale("RU"))

    val productOrderData
        get() = ProductOrderData(productId, price, count)

    val firstPictureUrl
        get() = pictures.firstOrNull()?.url
}