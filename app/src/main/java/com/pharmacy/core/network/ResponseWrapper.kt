package com.pharmacy.core.network

import android.content.Context
import androidx.annotation.StringRes
import com.pharmacy.R

@Deprecated("Now we using @Result@ sealed class")
sealed class ResponseWrapper<out T> {

    data class Success<out T>(val value: T) : ResponseWrapper<T>()

    data class Error(
        @StringRes val errorResId: Int? = null,
        val errorMessage: String? = null,
        val code: Int? = null
    ) : ResponseWrapper<Nothing>() {

        fun getMessage(context: Context): String =
            if (!errorMessage.isNullOrBlank()) errorMessage
            else if (errorResId != null) context.getString(errorResId)
            else context.getString(R.string.error_errorGettingData)
    }
}