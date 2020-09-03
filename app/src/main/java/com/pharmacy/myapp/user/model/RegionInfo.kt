package com.pharmacy.myapp.user.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName

@Entity
data class RegionInfo(
    @PrimaryKey
    @SerializedName("id") val regionId: Int,
    @SerializedName("name") val regionName: String
)