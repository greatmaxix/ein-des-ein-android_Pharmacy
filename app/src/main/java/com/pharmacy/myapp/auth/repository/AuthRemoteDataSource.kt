package com.pharmacy.myapp.auth.repository

import com.pharmacy.myapp.auth.model.Auth
import com.pharmacy.myapp.data.remote.RestConstants.CODE
import com.pharmacy.myapp.data.remote.RestConstants.EMAIL
import com.pharmacy.myapp.data.remote.RestConstants.NAME
import com.pharmacy.myapp.data.remote.RestConstants.PHONE
import com.pharmacy.myapp.data.remote.api.RestApi

class AuthRemoteDataSource(private val rm: RestApi) {

    suspend fun signIn(phone: String) = rm.signIn(mapOf(PHONE to phone))

    suspend fun signCode(phone: String, code: String) = rm.signCode(mapOf(PHONE to phone, CODE to code))

    suspend fun signUp(auth: Auth) = rm.signUp(mapOf(NAME to auth.name, EMAIL to auth.email, PHONE to auth.phone))
}