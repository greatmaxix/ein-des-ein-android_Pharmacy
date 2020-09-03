package com.pharmacy.myapp.user.profile

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.user.model.customerInfo.CustomerDAO
import com.pharmacy.myapp.user.model.customerInfo.CustomerInfo
import okhttp3.MultipartBody

class ProfileRepository(private val spManager: SPManager, private val rm: RestManager, private val dao: CustomerDAO) {

    fun getCustomerInfo() = dao.get()

    suspend fun updateCustomerInfo(name: String, email: String, avatarUuid: String) =
        safeApiCall(rm.tokenRefreshCall) {
            val updateCustomerInfo = rm.updateCustomerInfo(name, email, avatarUuid)
            rm.setLocalRegion(updateCustomerInfo.data.item.region?.regionId)
            saveCustomerInfo(updateCustomerInfo.data.item)
        }

    private suspend fun saveCustomerInfo(customer: CustomerInfo) = dao.update(customer)

    suspend fun logout() =
        safeApiCall(rm.tokenRefreshCall) {
            if (spManager.refreshToken.isNullOrEmpty()) throw Exception("Refresh token is empty")
            rm.logout(spManager.refreshToken ?: "")
        }

    suspend fun clearCustomerData(customer: CustomerInfo) {
        dao.delete(customer)
        spManager.clear()
    }

    suspend fun uploadImage(partBody: MultipartBody.Part) =
        safeApiCall(rm.tokenRefreshCall) { rm.uploadImage(partBody) }
}