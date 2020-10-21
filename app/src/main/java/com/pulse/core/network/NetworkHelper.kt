package com.pulse.core.network

import com.pulse.R
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@Deprecated("Now we are @GeneralErrorHandler@ for ")
suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
): ResponseWrapper<T> =
    try {
        ResponseWrapper.Success(apiCall())
    } catch (throwable: Exception) {
        Timber.e(throwable)
        when (throwable) {
            is SocketException,
            is UnknownHostException,
            is SocketTimeoutException -> ResponseWrapper.Error(R.string.error_serverNotResponding)
            is IOException -> ResponseWrapper.Error(R.string.error_networkErrorMessage)
            is HttpException -> ResponseWrapper.Error(R.string.error_networkErrorMessage)
            is ApiException -> ResponseWrapper.Error(
                R.string.error_errorGettingData,
                throwable.message
            )
            else -> ResponseWrapper.Error(R.string.error_errorGettingData)
        }
    }

fun getErrorMessage(responseBody: ResponseBody?): String = try {
    val jsonObject = JSONObject(responseBody!!.string())
    when {
        jsonObject.has("message") -> jsonObject.getString("message")
        jsonObject.has("error_type") -> jsonObject.getString("error_type")
        else -> "Something wrong happened"
    }
} catch (e: Exception) {
    "Something wrong happened"
}