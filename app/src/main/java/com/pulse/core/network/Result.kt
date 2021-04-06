package com.pulse.core.network

import com.pulse.data.GeneralException
import retrofit2.Response

sealed class Result<T> {
    companion object {
        fun <T> create(response: Response<T>): Result<T> = with(response) {
            if (isSuccessful) {
                ResultSuccess(body())
            } else {
                ResultError(GeneralException(errorBody()?.string() ?: message(), cause = null))
            }
        }
    }

    data class ResultError<T>(val exception: GeneralException) : Result<T>()
    data class ResultSuccess<T>(val body: T?) : Result<T>()
}