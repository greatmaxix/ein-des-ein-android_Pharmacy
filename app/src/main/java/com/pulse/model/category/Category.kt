package com.pulse.model.category

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Category(
    @PrimaryKey
    @SerializedName("code") var code: String,
    @SerializedName("name") var name: String? = null,
    var nestedCategories: List<String>? = null,
    @Ignore
    @SerializedName("nodes") var nodes: List<Category>? = null
) : Parcelable {
    constructor() : this("")
}