package com.pharmacy.model.order

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.pharmacy.R
import com.pharmacy.cart.model.CartProduct
import com.pharmacy.data.remote.model.order.CustomerOrderData
import com.pharmacy.data.remote.model.order.DeliveryInfoOrderData
import com.pharmacy.pharmacy.model.Pharmacy
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    @SerializedName("orderId") val id: Int,
    @SerializedName("status") val orderStatus: OrderStatus,
    @SerializedName("contactInfo") val contactInfo: CustomerOrderData,
    @SerializedName("deliveryInfo") val deliveryInfo: DeliveryInfoOrderData,
    @SerializedName("paymentType") val paymentType: String,
    @SerializedName("pharmacy") val pharmacy: Pharmacy,
    @SerializedName("pharmacyProductOrderDataList") val pharmacyProductOrderDataList: List<CartProduct>,
    @SerializedName("deliveryPrice") val deliveryPrice: Double,
    @SerializedName("pharmacyProductsTotalPrice") val pharmacyProductsTotalPrice: Double
) : Parcelable {

    val isShowProductCount
        get() = pharmacyProductOrderDataList.sumOf { it.pictures.size } == 0

    val productCount
        get() = pharmacyProductOrderDataList.size

    val productCountString
        get() = when (productCount) {
            1 -> R.string.oneGoods
            2, 3, 4 -> R.string.coupleOfGoods
            else -> R.string.multipleGoods
        }

    val isProductCountVisible
        get() = productCount > 3

    val getRestImagesCount
        get() = "+" + (pharmacyProductOrderDataList.size - 3).toString()
}