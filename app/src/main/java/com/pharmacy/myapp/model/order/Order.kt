package com.pharmacy.myapp.model.order

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.pharmacy.myapp.cart.model.CartProduct
import com.pharmacy.myapp.data.remote.model.order.CustomerOrderData
import com.pharmacy.myapp.data.remote.model.order.DeliveryInfoOrderData
import com.pharmacy.myapp.pharmacy.model.Pharmacy
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Order(
    @SerializedName("orderId") val id: Int,
    @SerializedName("status") val status: String,
    @SerializedName("contactInfo") val contactInfo: CustomerOrderData,
    @SerializedName("deliveryInfo") val deliveryInfo: DeliveryInfoOrderData,
    @SerializedName("paymentType") val paymentType: String,
    @SerializedName("pharmacy") val pharmacy: Pharmacy,
    @SerializedName("pharmacyProductOrderDataList") val pharmacyProductOrderDataList: List<CartProduct>,
    @SerializedName("deliveryPrice") val deliveryPrice: Double,
    @SerializedName("pharmacyProductsTotalPrice") val pharmacyProductsTotalPrice: Double) : Parcelable