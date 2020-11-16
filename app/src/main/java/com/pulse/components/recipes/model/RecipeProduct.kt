package com.pulse.components.recipes.model

import com.google.gson.annotations.SerializedName
import com.pulse.model.Picture

data class RecipeProduct(
    @SerializedName("globalProductId") val productId: Int,
    @SerializedName("rusName") val rusName: String,
    @SerializedName("releaseForm") val releaseForm: String,
    @SerializedName("pictures") val pictures: List<Picture>
)