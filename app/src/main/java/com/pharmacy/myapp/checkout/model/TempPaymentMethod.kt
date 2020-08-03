package com.pharmacy.myapp.checkout.model

import androidx.annotation.DrawableRes

data class TempPaymentMethod(
    val name: String,
    @DrawableRes val icon: Int
)