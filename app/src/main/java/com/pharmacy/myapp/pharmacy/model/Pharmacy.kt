package com.pharmacy.myapp.pharmacy.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pharmacy(
    val id: Int,
    val phone: String,
    val name: String,
    val location: PharmacyLocation,
    val logo: PharmacyLogo,
    val pharmacyProducts: List<PharmacyProducts>
) : Parcelable {

    val firstProductPrice
        get() = pharmacyProducts.first().price.toString()

}