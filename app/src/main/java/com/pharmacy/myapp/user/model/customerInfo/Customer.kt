package com.pharmacy.myapp.user.model.customerInfo

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.pharmacy.myapp.user.model.AvatarInfo
import com.pharmacy.myapp.user.model.RegionInfo

data class CustomerItem(@SerializedName("item") val item: Customer)

@Entity
data class Customer(
    @PrimaryKey
    @SerializedName("id") val id: Int,
    @Embedded
    @SerializedName("avatar") val avatarInfo: AvatarInfo?,
    @SerializedName("avatarUuid") val avatarUuid: String?,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("name") val name: String,
    @SerializedName("uuid") val uuid: String,
    @Embedded
    @SerializedName("region") val region: RegionInfo?
)