package com.pharmacy.myapp.data.remote.rest.request.order

import com.google.gson.annotations.SerializedName

data class CreateOrderRequest(
    @SerializedName("contactInfoCreateData") private val customerInfoOrderData: CustomerOrderData,
    @SerializedName("deliveryInfoCreateData") private val deliveryInfoOrderData: DeliveryInfoOrderData,
    @SerializedName("pharmacyId") private val pharmacyId: Int,
    @SerializedName("pharmacyProductOrderCreateDataList") private val productsListOrderData: List<ProductOrderData>,
    @SerializedName("deliveryPrice") private val deliveryPrice: Double,
    @SerializedName("paymentType") private val paymentType: String = "cash"
)