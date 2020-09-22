package com.pharmacy.myapp.pharmacy.model

import android.os.Parcelable
import com.pharmacy.myapp.model.Location
import com.pharmacy.myapp.model.Logo
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Pharmacy(
    val id: Int,
    val phone: String,
    val name: String,
    val location: Location,
    val logo: Logo,
    val pharmacyProducts: List<PharmacyProducts>
) : Parcelable {

    val firstProductPrice
        get() = pharmacyProducts.first().price.toString()

}