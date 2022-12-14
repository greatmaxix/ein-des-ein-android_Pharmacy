package com.pulse.data.remote.model.order

import com.google.gson.annotations.SerializedName

data class CreateOrderRequest(
    @SerializedName("contactInfo") private val customerInfoOrderData: CustomerOrderData,
    @SerializedName("deliveryInfo") private val deliveryInfoOrderData: DeliveryInfoOrderData,
    @SerializedName("pharmacyId") private val pharmacyId: Int,
    @SerializedName("pharmacyProductOrderCreateDataList") private val productsListOrderData: List<ProductOrderData>,
    @SerializedName("deliveryPrice") private val deliveryPrice: Double,
    @SerializedName("paymentType") private val paymentType: String = "cash"
)