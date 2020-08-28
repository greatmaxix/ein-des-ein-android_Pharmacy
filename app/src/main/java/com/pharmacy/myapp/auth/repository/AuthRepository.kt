package com.pharmacy.myapp.auth.repository

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.user.model.customerInfo.CustomerDAO
import com.pharmacy.myapp.user.model.customerInfo.CustomerInfo

class AuthRepository(private val spManager: SPManager, private val rm: RestManager, private val dao: CustomerDAO) {

    suspend fun signUp(name: String, phone: String, email: String) =
        safeApiCall(rm.tokenRefreshCall) { rm.signUp(name, phone, email).dataOrThrow() }

    suspend fun auth(phone: String) =
        safeApiCall(rm.tokenRefreshCall) { rm.auth(phone) }

    suspend fun login(phone: String, code: String) =
        safeApiCall(rm.tokenRefreshCall) { rm.login(phone, code).dataOrThrow() }

    fun saveToken(token: String, refreshToken: String) {
        spManager.token = token
        spManager.refreshToken = refreshToken
    }

    suspend fun saveCustomerInfo(customer: CustomerInfo): String? {
        dao.save(customer)
        return customer.avatarInfo?.url
    }
}