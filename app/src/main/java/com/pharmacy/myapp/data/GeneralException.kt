package com.pharmacy.myapp.data

import androidx.annotation.StringRes
import com.pharmacy.myapp.R

data class GeneralException(override val message: String, @StringRes val resId: Int = -1, val data: Any? = null) : Throwable() {

    companion object {
        fun someException() = GeneralException("some exception", R.string.error_networkErrorMessage)
    }

}