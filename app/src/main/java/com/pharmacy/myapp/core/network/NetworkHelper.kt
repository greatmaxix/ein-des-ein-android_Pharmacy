package com.pharmacy.myapp.core.network

import com.pharmacy.myapp.R
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import timber.log.Timber
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> safeApiCall(
    apiCall: suspend () -> T
) =
    try {
        ResponseWrapper.Success(apiCall.invoke())
    } catch (throwable: Exception) {
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
                    getErrorMessage(throwable.response()?.errorBody()),
                    code
                )
            }
            else -> ResponseWrapper.Error(R.string.error_errorGettingData)
        }
    }

fun getErrorMessage(responseBody: ResponseBody?): String = try {
    val jsonObject = JSONObject(responseBody!!.string())
    when {
        jsonObject.has("message") -> jsonObject.getString("message")
        jsonObject.has("type") -> jsonObject.getString("type")
        else -> "Something wrong happened"
    }
} catch (e: Exception) {
    "Something wrong happened"
}