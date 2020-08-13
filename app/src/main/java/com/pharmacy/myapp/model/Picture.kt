package com.pharmacy.myapp.model

import com.google.gson.annotations.SerializedName

data class Picture(
    @SerializedName("url") val url: String,
    @SerializedName("uuid") val uuid: String
)