package com.pharmacy.myapp.profile

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.data.remote.rest.response.UserDataResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody

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

    suspend fun uploadImage(createFormData: MultipartBody.Part) =
        safeApiCall { rm.uploadImage(createFormData) }

    suspend fun uploadImage(map: HashMap<String, RequestBody>) =
        safeApiCall { rm.uploadImage(map) }

}