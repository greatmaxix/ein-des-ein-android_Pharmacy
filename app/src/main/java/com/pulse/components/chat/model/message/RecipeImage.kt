package com.pulse.components.chat.model.message

import android.os.Parcelable
import androidx.room.ColumnInfo
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class RecipeImage(
    @ColumnInfo(name = "recipeUuid")
    @SerializedName("uuid") val uuid: String?,
    @SerializedName("originalFilename") val originalFilename: String?
) : Parcelable