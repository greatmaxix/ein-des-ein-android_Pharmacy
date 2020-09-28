package com.pharmacy.myapp.data.remote.rest.request.order

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class DeliveryInfoOrderData(
    @SerializedName("type") private val type: String?,
    @SerializedName("comment") private val comment: String?,
    @SerializedName("addressCreateData") private val addressOrderData: AddressOrderData?
) : Parcelable