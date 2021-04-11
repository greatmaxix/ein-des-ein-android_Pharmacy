package com.pulse.data.remote

import androidx.annotation.StringRes
import com.pulse.R

enum class APIErrors(val key: String, @StringRes val errorResId: Int, val isFieldError: Boolean = false) {
    UNKNOWN_ERROR("", R.string.error_errorGettingData);

    companion object {

        fun getByKey(key: String) = values().firstOrNull { it.key == key } ?: UNKNOWN_ERROR
    }
}