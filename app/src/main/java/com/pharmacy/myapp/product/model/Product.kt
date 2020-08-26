package com.pharmacy.myapp.product.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
class Product(
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("activeSubstances") val substances: List<String>
) : ProductLite(), Parcelable {

    val substance
        get() = substances.first()

    val getFullManufacture
        get() = "${manufacture.producer} , ${manufacture.producerRu}"

}