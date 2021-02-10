package com.pulse.components.cart.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CartProductInfo(
    @SerializedName("id") val id: Int
) : Parcelable