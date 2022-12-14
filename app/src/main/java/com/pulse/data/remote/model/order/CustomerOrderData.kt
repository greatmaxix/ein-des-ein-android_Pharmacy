package com.pulse.data.remote.model.order

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class CustomerOrderData(
    @SerializedName("name") val name: String?,
    @SerializedName("phone") val phone: String?,
    @SerializedName("email") val email: String?,
    @SerializedName("delegateFullName") val delegateFullName: String?
) : Parcelable