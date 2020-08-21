package com.pharmacy.myapp.profile

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.model.customerInfo.CustomerDAO
import com.pharmacy.myapp.model.customerInfo.CustomerInfo
import okhttp3.MultipartBody

class ProfileRepository(private val spManager: SPManager, private val rm: RestManager, private val dao: CustomerDAO) {

    fun getCustomerInfo() = dao.get()

    suspend fun updateCustomerInfo(name: String, email: String, avatarUuid: String) =
        safeApiCall(rm.tokenRefreshCall) {
            saveCustomerInfo(rm.updateCustomerInfo(name, email, avatarUuid).data.item)
        }

    private suspend fun saveCustomerInfo(customer: CustomerInfo) = dao.update(customer)

    suspend fun logout() =
        safeApiCall(rm.tokenRefreshCall) {
            if (spManager.refreshToken.isNullOrEmpty()) throw Exception("Refresh token is empty")
            rm.logout(spManager.refreshToken ?: "")
        }

    fun clearCustomerData(customer: CustomerInfo) {
        dao.delete(customer)
        spManager.clear()
    }

    suspend fun uploadImage(partBody: MultipartBody.Part) =
        safeApiCall(rm.tokenRefreshCall) { rm.uploadImage(partBody) }
}