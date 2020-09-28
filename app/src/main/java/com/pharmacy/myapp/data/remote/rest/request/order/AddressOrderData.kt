package com.pharmacy.myapp.data.remote.rest.request.order

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class AddressOrderData(
    @SerializedName("city") var city: String? = "",
    @SerializedName("street") var street: String? = "",
    @SerializedName("house") var house: String? = "",
    @SerializedName("apartment") var apartment: String? = ""
) : Parcelable