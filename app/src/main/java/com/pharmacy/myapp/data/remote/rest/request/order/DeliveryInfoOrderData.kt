package com.pharmacy.myapp.data.remote.rest.request.order

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeliveryInfoOrderData(
    @SerializedName("type") val deliveryType: DeliveryType?,
    @SerializedName("comment") val comment: String?,
    @SerializedName("address") val addressOrderData: AddressOrderData?
) : Parcelable