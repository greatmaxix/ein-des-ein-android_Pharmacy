package com.pharmacy.myapp.auth.repository

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.remote.RestManager

class AuthRemoteDataSource(private val rm: RestManager) {

    suspend fun signUp(name: String, phone: String, email: String) =
        safeApiCall { rm.signUp(name, phone, email).dataOrThrow() }

    suspend fun auth(phone: String) =
        safeApiCall{ rm.auth(phone) }

    suspend fun login(phone: String, code: String) =
        safeApiCall { rm.login(phone, code).dataOrThrow() }
}