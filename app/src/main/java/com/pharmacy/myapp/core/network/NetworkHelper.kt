package com.pharmacy.myapp.core.network

import com.pharmacy.myapp.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> T
): ResponseWrapper<T> {
    return withContext(dispatcher) {
        try {
            ResponseWrapper.Success(apiCall.invoke())
        } catch (throwable: Throwable) {
            Timber.e(throwable)
            when (throwable) {
                is SocketException,
                is UnknownHostException,
                is SocketTimeoutException -> ResponseWrapper.Error(R.string.error_serverNotResponding)
                is IOException -> ResponseWrapper.Error(R.string.error_networkErrorMessage)
                is HttpException -> {
                    val code = throwable.code()
//                    Crashlytics.log(Log.WARN, "ErrorHandler", errorResponse?.errorMessage)
                    ResponseWrapper.Error(
                        R.string.error_networkErrorMessage,
                        throwable.message(),
                        code
                    )
                }
                else -> ResponseWrapper.Error(R.string.error_errorGettingData)
            }
        }
    }
}