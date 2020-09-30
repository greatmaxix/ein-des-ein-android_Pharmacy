package com.pharmacy.myapp.data.remote.model.order

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class CustomerOrderData(
    @SerializedName("name") private val name: String?,
    @SerializedName("phone") private val phone: String?,
    @SerializedName("email") private val email: String?
) : Parcelable