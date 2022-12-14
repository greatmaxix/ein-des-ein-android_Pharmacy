package com.pulse.data

import com.google.gson.Gson
import com.pulse.R
import com.pulse.data.remote.ErrorModel
import com.pulse.model.Violation
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.get
import retrofit2.HttpException
import timber.log.Timber
import java.net.SocketException
import java.net.SocketTimeoutException
import java.net.UnknownHostException

@OptIn(KoinApiExtension::class)
class GeneralErrorHandler : KoinComponent {

    fun checkThrowable(throwable: Throwable) = when (throwable) {
        is SocketException, is UnknownHostException, is SocketTimeoutException -> networkError
        is HttpException -> httpError(throwable)
        is GeneralException -> throwable
        else -> unknownError
    }.also { Timber.e(throwable) }

    private val networkError
        get() = GeneralException("network", R.string.error_serverNotResponding, cause = null)

    private val unknownError
        get() = GeneralException("unknown", R.string.error_errorGettingData, cause = null)

    private fun <T> errorBody(error: HttpException, type: Class<T>) =
        error.response()?.errorBody()?.run { get<Gson>().fromJson(string(), type) }

    //TODO have to add valid response for errors
    private fun httpError(error: HttpException): GeneralException {
        val errorModel = errorBody(error, ErrorModel::class.java)
        return when (errorModel?.type) {
            "validation_error" -> errorModel.violations?.first()?.wrapValidationError() ?: unknownError
            "authentication_failure" -> GeneralException(" ", R.string.authErrorEnteredValue, cause = error)
            "not_found" -> {
                when {
                    errorModel.message == "This item is currently unavailable" -> GeneralException(" ", R.string.notFoundCurrentlyUnavailable, cause = error)
                    errorModel.message.contains("Customer with phone", true) -> GeneralException(" ", R.string.authCustomerWithThisPhoneNumber, cause = error)
                    else -> unknownError
                }
            }
            else -> unknownError
        }
    }
}

fun Violation.wrapValidationError(): GeneralException = when {
    propertyPath == "phone" && message == "This customer exist." -> GeneralException(" ", R.string.authCustomerAlreadyExist, cause = null)
    propertyPath == "phone" && message == "This value is not valid." -> GeneralException(" ", R.string.authEnteredValueNotValid, cause = null)
    else -> GeneralException("unknown", R.string.error_errorGettingData, cause = null)
}