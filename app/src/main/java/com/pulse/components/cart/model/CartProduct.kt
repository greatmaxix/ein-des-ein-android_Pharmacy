package com.pulse.components.cart.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.pulse.App
import com.pulse.data.remote.model.order.ProductOrderData
import com.pulse.model.Picture
import com.pulse.product.model.Manufacture
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
    @SerializedName("productCount") var count: Int,
    var needShowDivider: Boolean
) : Parcelable {
    //TODO Create global "Local helper"
    val productLocale: String?
        get() = App.localeMap[manufacture.isoCode]?.getDisplayCountry(Locale("RU"))

    val productOrderData
        get() = ProductOrderData(productId, price, count)

    val firstPictureUrl
        get() = pictures.firstOrNull()?.url
}