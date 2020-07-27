package com.pharmacy.myapp.profile

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.model.CustomerInfo
import com.pharmacy.myapp.model.CustomerResponse
import okhttp3.MultipartBody

class ProfileRepository(private val spManager: SPManager, private val rm: RestManager) {

    fun getCustomerInfo() =
        CustomerInfo(spManager.email, spManager.name, spManager.phone, spManager.avatarUuid)

    suspend fun updateCustomerInfo(name: String, email: String, avatarUuid: String) =
        safeApiCall(rm.tokenRefreshCall) {
            saveCustomerInfo(rm.updateCustomerInfo(name, email, avatarUuid).data)
        }

    private fun saveCustomerInfo(response: CustomerResponse) {
        spManager.email = response.email
        spManager.name = response.name
        spManager.avatarUuid = response.avatarInfo.uuid
        spManager.avatarUrl = response.avatarInfo.url
    }

    suspend fun logout() =
        safeApiCall(rm.tokenRefreshCall) {
            if (spManager.refreshToken.isNullOrEmpty()) throw Exception("Refresh token is empty")
            rm.logout(spManager.refreshToken ?: "")
        }

    fun clearCustomerData() = spManager.clear()

    suspend fun uploadImage(partBody: MultipartBody.Part) =
        safeApiCall(rm.tokenRefreshCall) { rm.uploadImage(partBody) }
}