package com.pharmacy.myapp.data.remote.authenticator

import com.pharmacy.myapp.data.remote.repository.RESTRepository
import okhttp3.Authenticator
import okhttp3.Response
import okhttp3.Route
import org.koin.core.KoinComponent
import org.koin.core.inject

class RESTAuthenticator : Authenticator, KoinComponent {

    private val repository: RESTRepository by inject()

    override fun authenticate(route: Route?, response: Response) = with(response) {
        if (isContainsAuthorization) {
            val retryCount = request.header("RetryCount")?.toInt() ?: 0
            if (retryCount > 2) {
                //TODO need force logout
                null
            } else {
                request
                    .newBuilder()
                    .header("Authorization", "Bearer: ${repository.refreshToken()}")
                    .header("RetryCount", "$retryCount")
                    .build()
            }
        } else {
            null
        }
    }

    private val Response.isContainsAuthorization
        get() = request.header("Authorization")?.startsWith("Bearer: ") ?: false
}