package com.pulse.chat.model.message

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class RecipeImage(
    @ColumnInfo(name = "recipeUuid")
    @SerializedName("uuid") val uuid: String?,
    @SerializedName("originalFilename") val originalFilename: String?
) : Parcelable