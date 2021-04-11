package com.pulse.data

import androidx.annotation.StringRes
import com.pulse.R
import com.pulse.model.BaseDataResponse

data class GeneralException(override val message: String, @StringRes val resId: Int = 0, val data: BaseDataResponse<*>? = null, override val cause: Throwable?) : Throwable() {

    companion object {
        fun someException() = GeneralException("some exception", R.string.error_networkErrorMessage, cause = null)

        val needToLoginCart = GeneralException("Items from cart", R.string.forCheckCart, cause = null)

        val needToLoginAdd = GeneralException("Add to cart", R.string.forAddingToCart, cause = null)

        val needToLoginRecipes = GeneralException("Recipes list", R.string.toSeeYourRecipes, cause = null)
    }
}