package com.pulse.core.utils

import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds

object BoundsUtils {

    fun boundsFromLatLngList(list: List<LatLng>): LatLngBounds {
        var bottomRightLatitude = list.first().latitude
        var topLeftLatitude = bottomRightLatitude
        var bottomRightLongitude = list.first().longitude
        var topLeftLongitude = bottomRightLongitude
        for (latLng in list) {
            if (latLng.latitude > topLeftLatitude) topLeftLatitude = latLng.latitude
            if (latLng.latitude < bottomRightLatitude) bottomRightLatitude = latLng.latitude
            if (latLng.longitude > topLeftLongitude) topLeftLongitude = latLng.longitude
            if (latLng.longitude < bottomRightLongitude) bottomRightLongitude = latLng.longitude
        }
        return LatLngBounds(LatLng(bottomRightLatitude, bottomRightLongitude), LatLng(topLeftLatitude, topLeftLongitude))
    }
}