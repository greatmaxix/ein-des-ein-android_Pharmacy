package com.pharmacy.myapp.auth

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.model.customerInfo.CustomerDAO
import com.pharmacy.myapp.model.customerInfo.CustomerInfo

class AuthRepository(private val spManager: SPManager, private val rm: RestManager, private val dao: CustomerDAO) {

    suspend fun signUp(name: String, phone: String, email: String) =
        safeApiCall(rm.tokenRefreshCall) {
            val response = rm.signUp(name, phone, email)
            response.dataOrThrow()
        }

    suspend fun auth(phone: String) =
        safeApiCall(rm.tokenRefreshCall) { rm.auth(phone) }

    suspend fun login(phone: String, code: String) =
        safeApiCall(rm.tokenRefreshCall) {
            val response = rm.login(phone, code)
            response.dataOrThrow()
        }

    fun saveToken(token: String, refreshToken: String) {
        spManager.token = token
        spManager.refreshToken = refreshToken
    }

    suspend fun saveCustomerInfo(customer: CustomerInfo): String? {
        dao.save(customer)
        return customer.avatarInfo?.url
    }
}