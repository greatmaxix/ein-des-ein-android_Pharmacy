package com.pharmacy.myapp.model.category

import com.google.gson.annotations.SerializedName

data class Category(
    @SerializedName("code") val code: String,
    @SerializedName("rusName") val rusName: String,
    @SerializedName("nodes") val nodes: List<Category>
)