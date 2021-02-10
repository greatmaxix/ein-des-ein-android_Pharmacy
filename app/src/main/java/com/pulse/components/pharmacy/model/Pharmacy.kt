package com.pulse.components.pharmacy.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.pulse.model.Location
import com.pulse.model.Picture
import kotlinx.parcelize.Parcelize

@Parcelize
data class Pharmacy(
    @SerializedName("id") val id: Int,
    @SerializedName("phone") val phone: String,
    @SerializedName("name") val name: String,
    @SerializedName("location") val location: Location,
    @SerializedName("logo") val logo: Picture,
    @SerializedName("pharmacyProducts") val pharmacyProducts: List<PharmacyProducts>?
) : Parcelable {

    val firstProductPrice
        get() = pharmacyProducts?.first()?.price.toString()

}