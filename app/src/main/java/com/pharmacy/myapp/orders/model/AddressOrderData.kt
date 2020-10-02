package com.pharmacy.myapp.orders.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressOrderData(
    @SerializedName("city") var city: String? = "",
    @SerializedName("street") var street: String? = "",
    @SerializedName("house") var house: String? = "",
    @SerializedName("apartment") var apartment: String? = ""
) : Parcelable {

    val streetAndCity
        get() = "$street, $city"

    val houseAndApartment
        get() = "$house, $apartment"

}