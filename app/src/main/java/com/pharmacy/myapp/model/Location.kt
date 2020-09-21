package com.pharmacy.myapp.model

import android.os.Parcelable
import com.google.android.gms.maps.model.LatLng
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Location(
    val lat: Double,
    val lng: Double,
    val address: String
) : Parcelable {
    val mapCoordinates
        get() = LatLng(lat, lng)
}