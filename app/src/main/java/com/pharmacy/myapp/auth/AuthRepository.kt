package com.pharmacy.myapp.auth

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager
import kotlinx.coroutines.Dispatchers

class AuthRepository(private val spManager: SPManager, private val rm: RestManager) {

    suspend fun signUp(name: String, phone: String, email: String) =
        safeApiCall(Dispatchers.IO) { rm.signUp(name, phone, email) }

    suspend fun auth(phone: String) = safeApiCall(Dispatchers.IO) { rm.auth(phone) }

    suspend fun login(phone: String, code: String) = safeApiCall(Dispatchers.IO) { rm.login(phone, code) }

}