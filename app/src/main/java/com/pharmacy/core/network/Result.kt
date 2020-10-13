package com.pharmacy.core.network

import com.pharmacy.data.GeneralException
import retrofit2.Response

sealed class Result<T> {
    companion object {
        fun <T> create(response: Response<T>): Result<T> = with(response) {
            if (isSuccessful) {
                ResultSuccess(body())
            } else {
                ResultError(GeneralException(errorBody()?.string() ?: message()))
            }
        }
    }

    data class ResultError<T>(val exception: GeneralException) : Result<T>()
    data class ResultSuccess<T>(val body: T?) : Result<T>()
}