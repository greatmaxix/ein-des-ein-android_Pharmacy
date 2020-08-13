package com.pharmacy.myapp.model

import com.google.gson.annotations.SerializedName

data class Region(
    @SerializedName("children") val children: List<Region>,
    @SerializedName("id") val id: Int,
    @SerializedName("lvl") val lvl: Int,
    @SerializedName("name") val name: String
)