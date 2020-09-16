package com.pharmacy.myapp.pharmacy.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class PharmacyLocation(
    val lat: Double,
    val lng: Double,
    val address: String
) : Parcelable {
    val mapCoordinates
        get() = LatLng(lat, lng)
}