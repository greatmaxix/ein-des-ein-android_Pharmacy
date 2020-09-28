package com.pharmacy.myapp.pharmacy.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.pharmacy.myapp.model.Location
import com.pharmacy.myapp.model.Logo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pharmacy(
    @SerializedName("id") val id: Int,
    @SerializedName("phone") val phone: String,
    @SerializedName("name") val name: String,
    @SerializedName("location") val location: Location,
    @SerializedName("logo") val logo: Logo,
    @SerializedName("pharmacyProducts") val pharmacyProducts: List<PharmacyProducts>
) : Parcelable {

    val firstProductPrice
        get() = pharmacyProducts.first().price.toString()

}