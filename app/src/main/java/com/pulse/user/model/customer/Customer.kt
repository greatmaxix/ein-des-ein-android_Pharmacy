package com.pulse.user.model.customer

import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.pulse.user.model.RegionInfo
import com.pulse.user.model.avatar.Avatar

data class CustomerItem(@SerializedName("item") val item: Customer)

@Entity
data class Customer(
    @PrimaryKey
    @SerializedName("id") val id: Int,
    @Embedded
    @SerializedName("avatar") val avatarInfo: Avatar?,
    @SerializedName("avatarUuid") val avatarUuid: String?,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("email") val email: String?,
    @SerializedName("phone") val phone: String,
    @SerializedName("name") val name: String,
    @SerializedName("uuid") val uuid: String,
    @Embedded
    @SerializedName("region") val region: RegionInfo?
)