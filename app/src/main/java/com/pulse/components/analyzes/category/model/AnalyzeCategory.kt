package com.pulse.components.analyzes.category.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.Ignore
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity
data class AnalyzeCategory(
    @SerializedName("id") var id: Long,
    @SerializedName("name") var name: String? = null,
    @SerializedName("code") var code: String? = null,
    @SerializedName("nodes") var nodes: List<AnalyzeCategory>? = null,
    @Ignore
    @SerializedName("topLevel") var topLevel: Boolean = false
) : Parcelable