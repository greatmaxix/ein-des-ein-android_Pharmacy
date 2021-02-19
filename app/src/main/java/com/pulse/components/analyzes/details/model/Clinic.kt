package com.pulse.components.analyzes.details.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import com.pulse.model.Location
import com.pulse.model.Picture
import kotlinx.parcelize.Parcelize

@Parcelize
data class Clinic(
    @SerializedName("id") val id: Int,
    @SerializedName("phone") val phone: String,
    @SerializedName("name") val name: String,
    @SerializedName("description") val description: String,
    @SerializedName("rating") val rating: Double,
    @SerializedName("location") val location: Location,
    @SerializedName("logo") val logo: Picture,
    @SerializedName("servicePrice") val servicePrice: Int
) : Parcelable