package com.pharmacy.myapp.data.remote.authenticator

import com.pharmacy.myapp.data.remote.repository.RestRepository
import okhttp3.Authenticator
import okhttp3.Response
import okhttp3.Route
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class RestAuthenticator : Authenticator, KoinComponent {

    private val repository: RestRepository by inject()

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