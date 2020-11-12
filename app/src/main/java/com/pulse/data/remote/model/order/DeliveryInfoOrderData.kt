package com.pulse.data.remote.model.order

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "address")
data class DeliveryInfoOrderData(
    @SerializedName("comment") var comment: String?,
    @Embedded
    @SerializedName("address") var addressOrderData: AddressOrderData?,
    @SerializedName("type")
    @Ignore
    var deliveryType: DeliveryType? = null,
    @PrimaryKey
    @SerializedName("id") var id: Int = 0,
) : Parcelable {

    constructor() : this(null, null, null)
}