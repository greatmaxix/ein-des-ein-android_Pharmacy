package com.pharmacy.checkout.model

data class TempProductModel(
    val imageUrl: String,
    val name: String,
    val description: String,
    val issuer: String,
    val price: String,
    val count: Int
)