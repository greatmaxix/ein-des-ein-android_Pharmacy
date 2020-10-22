package com.pulse.chat.model.message

import android.os.Parcelable
import androidx.room.Embedded
import com.google.gson.annotations.SerializedName
import com.pulse.model.Picture
import com.pulse.product.model.Aggregation
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MessageProduct(
    @SerializedName("globalProductId") val globalProductId: Int,
    @SerializedName("rusName") val rusName: String,
    @SerializedName("releaseForm") val releaseForm: String,
    @SerializedName("pictures") val pictures: List<Picture> = listOf(),
    @Embedded
    @SerializedName("pharmacyProductsAggregationData") val pharmacyProductsAggregationData: Aggregation?
) : Parcelable