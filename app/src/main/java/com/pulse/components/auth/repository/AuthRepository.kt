package com.pulse.components.auth.repository

import com.pulse.components.auth.model.Auth
import com.pulse.data.local.SPManager
import com.pulse.user.model.customer.Customer
import com.pulse.user.model.customer.CustomerDAO

class AuthRepository(private val spManager: SPManager, private val dao: CustomerDAO, private val rds: AuthRemoteDataSource) {

    suspend fun signUp(auth: Auth) = rds.signUp(auth).dataOrThrow()

    suspend fun signIn(phone: String) = rds.signIn(phone).dataOrThrow()

    suspend fun signCode(phone: String, code: String) = with(rds.signCode(phone, code).dataOrThrow()) {
        saveToken(token, refreshToken)
        saveCustomer(customer)
    }

    private fun saveToken(token: String, refreshToken: String) {
        spManager.token = token
        spManager.refreshToken = refreshToken
    }

    private suspend fun saveCustomer(customer: Customer): String? {
        dao.insert(customer)
        return customer.avatarInfo?.url
    }
}