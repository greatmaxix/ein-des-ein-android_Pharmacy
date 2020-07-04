package com.pharmacy.myapp.core.network

import com.pharmacy.myapp.R
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response
import timber.log.Timber
import java.io.IOException
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

suspend fun <T> safeApiCall(
    dispatcher: CoroutineDispatcher,
    apiCall: suspend () -> Response<T>
): ResponseWrapper<Response<T>> {
    return withContext(dispatcher) {
        try {
            val value = apiCall.invoke()
            // тут как будешь смотреть, позвони я тебе расскажу, все идет к тому что хочется использовать эту либу, чтобы все это руками не писать
            // https://github.com/haroldadmin/NetworkResponseAdapter
            // временно сделал так
            if (!value.isSuccessful) {
                return@withContext ResponseWrapper.Error(
                    R.string.error_networkErrorMessage,
                    getErrorMessage(value.errorBody()),
                    value.code()
                )
            }
            return@withContext ResponseWrapper.Success(value)
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