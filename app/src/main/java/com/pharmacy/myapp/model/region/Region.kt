package com.pharmacy.myapp.model.region

import com.google.gson.annotations.SerializedName

data class Region(
    @SerializedName("nodes") val children: List<Region>,
    @SerializedName("id") val id: Int,
    @SerializedName("lvl") val lvl: Int,
    @SerializedName("name") val name: String
) {
    val firstCharOfName
    get()  = name.first()

}