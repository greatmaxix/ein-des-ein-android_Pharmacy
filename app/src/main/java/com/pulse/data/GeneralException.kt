package com.pulse.data

import androidx.annotation.StringRes
import com.pulse.R

data class GeneralException(override val message: String, @StringRes val resId: Int = -1, val data: Any? = null) : Throwable() {

    companion object {
        fun someException() = GeneralException("some exception", R.string.error_networkErrorMessage)

        val needToLoginCart = GeneralException("Items from cart", R.string.forCheckCart)

        val needToLoginAdd = GeneralException("Add to cart", R.string.forAddingToCart)
    }
}