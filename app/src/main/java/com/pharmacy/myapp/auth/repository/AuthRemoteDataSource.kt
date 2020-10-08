package com.pharmacy.myapp.auth.repository

import com.pharmacy.myapp.auth.model.SignUp
import com.pharmacy.myapp.data.remote.RestConstants.CODE
import com.pharmacy.myapp.data.remote.RestConstants.EMAIL
import com.pharmacy.myapp.data.remote.RestConstants.NAME
import com.pharmacy.myapp.data.remote.RestConstants.PHONE
import com.pharmacy.myapp.data.remote.api.RestApi

class AuthRemoteDataSource(private val rm: RestApi) {

    suspend fun signIn(phone: String) = rm.signIn(mapOf(PHONE to phone))

    suspend fun checkCode(phone: String, code: String) = rm.checkCode(mapOf(PHONE to phone, CODE to code))

    suspend fun signUp(signUp: SignUp) = rm.signUp(mapOf(NAME to signUp.name, EMAIL to signUp.email, PHONE to signUp.phone))
}