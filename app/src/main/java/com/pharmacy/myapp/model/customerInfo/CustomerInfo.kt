package com.pharmacy.myapp.model.customerInfo

import android.os.Parcelable
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import com.pharmacy.myapp.model.AvatarInfo
import kotlinx.android.parcel.Parcelize

@Entity
@Parcelize
data class CustomerInfo(
    @PrimaryKey
    @SerializedName("id") val id: Int,
    @Embedded
    @SerializedName("avatar") val avatarInfo: AvatarInfo?,
    @SerializedName("avatarUuid") val avatarUuid: String,
    @SerializedName("createdAt") val createdAt: String,
    @SerializedName("email") val email: String,
    @SerializedName("phone") val phone: String,
    @SerializedName("username") val name: String,
    @SerializedName("uuid") val uuid: String
): Parcelable