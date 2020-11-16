package com.pulse.components.recipes.model

import com.google.gson.annotations.SerializedName

data class RecipeProducts(
    @SerializedName("globalProduct") val products: RecipeProduct,
    @SerializedName("count") val count: Int
)