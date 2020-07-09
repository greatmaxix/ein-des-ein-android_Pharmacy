package com.pharmacy.myapp.profile

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.data.remote.rest.response.UserDataResponse
import kotlinx.coroutines.Dispatchers

class ProfileRepository(private val spManager: SPManager, private val rm: RestManager) {

    fun getUserData() = Triple(spManager.email, spManager.phone, spManager.username)

    suspend fun updateCustomerData(name: String, email: String) = safeApiCall(Dispatchers.IO) {
        rm.updateCustomerData(name, email)
    }

    fun saveNewUserData(dataResponse: UserDataResponse) {
        spManager.email = dataResponse.email
        spManager.username = dataResponse.username
    }
}