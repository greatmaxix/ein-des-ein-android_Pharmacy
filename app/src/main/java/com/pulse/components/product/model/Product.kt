package com.pulse.components.product.model

import android.os.Parcelable
import androidx.room.Entity
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
class Product(
    @SerializedName("description") val description: String,
    @SerializedName("category") val category: String,
    @SerializedName("activeSubstances") val substances: List<String>
) : ProductLite(), Parcelable {

    val substance
        get() = if (substances.isEmpty()) "" else substances.firstOrNull()

    val getFullManufacture
        get() = "${manufacture.producer} , $productLocale"

}