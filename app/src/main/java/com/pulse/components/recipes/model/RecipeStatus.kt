package com.pulse.components.recipes.model

enum class RecipeStatus(val status: String) {
    ACTIVE("active"), CLOSED("closed");

    val isActive
        get() = this == ACTIVE

    companion object {
        fun getRecipeStatus(status: String) = values().find { it.status == status }
    }
}