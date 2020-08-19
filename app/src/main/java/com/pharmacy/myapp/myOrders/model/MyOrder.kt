package com.pharmacy.myapp.myOrders.model

data class MyOrder(
    val id: Int,
    val status: String,
    val deliveryType: String,
    val dateTime: String,
    val address: String,
    val images: List<String>?,
    val price: String
)