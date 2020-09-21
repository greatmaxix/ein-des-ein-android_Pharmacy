package com.pharmacy.myapp.model.category

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Category(
    @SerializedName("code") val code: String,
    @SerializedName("name") val name: String,
    @SerializedName("nodes") val nodes: List<Category>
) : Parcelable