package com.pulse.components.recipes.model

import com.google.gson.annotations.SerializedName

data class Recipe(
    @SerializedName("id") val id: Int,
    @SerializedName("doctor") val doctor: Doctor?,
    @SerializedName("status") val status: RecipeStatus,
    @SerializedName("code") val code: String,
    @SerializedName("validTill") val validTill: String,
    @SerializedName("recipeProducts") val products: List<RecipeProducts>
) {

    val productUrl
        get() = products.firstOrNull()?.products?.pictures?.firstOrNull()?.url

    val productRusName
        get() = products.firstOrNull()?.products?.rusName

    val productCount
        get() = products.firstOrNull()?.count

    val doctorName
        get() = doctor?.name

    val doctorUrl
        get() = doctor?.avatar?.url
}