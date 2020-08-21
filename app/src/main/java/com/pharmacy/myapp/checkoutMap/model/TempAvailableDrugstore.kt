package com.pharmacy.myapp.checkoutMap.model

import com.google.android.gms.maps.model.LatLng
import com.pharmacy.myapp.R

data class TempAvailableDrugstore(
    var id: Int,
    var availability: String,
    val name: String,
    val address: String,
    val contactInfo: String,
    val workingHours: String,
    val price: String,
    val latLng: LatLng
) {
    fun availabilityColor() = when (availability) {
        "Все в наличии" -> R.color.colorAccent
        "3/4 в наличии" -> R.color.orange
        else -> R.color.primaryBlue
    }
}