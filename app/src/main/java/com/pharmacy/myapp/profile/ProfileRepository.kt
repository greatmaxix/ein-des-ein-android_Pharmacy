package com.pharmacy.myapp.profile

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.data.remote.rest.response.UserDataResponse

class ProfileRepository(private val spManager: SPManager, private val rm: RestManager) {

    fun getUserData() = Triple(spManager.email, spManager.phone, spManager.username)

    suspend fun updateCustomerData(name: String, email: String) =
        safeApiCall(rm.tokenRefreshCall) {
            rm.updateCustomerData(name, email)
        }

    fun saveNewUserData(dataResponse: UserDataResponse) {
        spManager.email = dataResponse.email
        spManager.username = dataResponse.username
    }

    suspend fun logout() =
        safeApiCall(rm.tokenRefreshCall) {
            if (spManager.refreshToken.isNullOrEmpty()) throw Exception("Refresh token is empty")
            rm.logout(spManager.refreshToken ?: "")
        }

    fun clearCustomerData() = spManager.clear()

}