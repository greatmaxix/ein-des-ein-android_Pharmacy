package com.pharmacy.myapp.cart.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CartProductInfo(
    @SerializedName("id") val id: Int,
    @SerializedName("count") var count: Int
) : Parcelable