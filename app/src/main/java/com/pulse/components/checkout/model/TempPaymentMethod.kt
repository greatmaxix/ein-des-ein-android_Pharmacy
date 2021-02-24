package com.pulse.components.checkout.model

import android.os.Parcelable
import androidx.annotation.DrawableRes
import kotlinx.parcelize.Parcelize

@Parcelize
data class TempPaymentMethod(
    val name: String,
    @DrawableRes val icon: Int,
    val isChecked: Boolean = false
) : Parcelable