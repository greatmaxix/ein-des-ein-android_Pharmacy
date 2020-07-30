package com.pharmacy.myapp.auth

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.model.customerInfo.CustomerDAO
import com.pharmacy.myapp.model.customerInfo.CustomerInfo
import retrofit2.HttpException
import java.net.HttpURLConnection.HTTP_CREATED

class AuthRepository(private val spManager: SPManager, private val rm: RestManager, private val dao: CustomerDAO) {

    suspend fun signUp(name: String, phone: String, email: String) =
        safeApiCall(rm.tokenRefreshCall) {
            val response = rm.signUp(name, phone, email)
            if (response.isSuccessful && response.code() == HTTP_CREATED) {
                response.body()
            } else {
                throw HttpException(response)
            }
        }

    suspend fun auth(phone: String) =
        safeApiCall(rm.tokenRefreshCall) { rm.auth(phone) }

    suspend fun login(phone: String, code: String) =
        safeApiCall(rm.tokenRefreshCall) { rm.login(phone, code) }

    fun saveToken(token: String, refreshToken: String) {
        spManager.token = token
        spManager.refreshToken = refreshToken
    }

    suspend fun updateCustomerInfo() = safeApiCall(rm.tokenRefreshCall) {
        saveCustomerInfo(rm.fetchCustomerInfo().data)
    }

    private suspend fun saveCustomerInfo(customer: CustomerInfo): String? {
        dao.save(customer)
        return customer.avatarInfo?.url
    }
}