package com.pulse.checkout.model

import androidx.annotation.DrawableRes

data class TempPaymentMethod(
    val name: String,
    @DrawableRes val icon: Int,
    val isChecked: Boolean = false
)