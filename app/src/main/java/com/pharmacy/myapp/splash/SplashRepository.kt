package com.pharmacy.myapp.splash

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.model.CustomerResponse

class SplashRepository(private val rm: RestManager, private val spManager: SPManager) {

    suspend fun updateCustomerInfo() = safeApiCall(rm.tokenRefreshCall) {
        saveCustomerInfo(rm.fetchCustomerInfo().data)
    }

    private fun saveCustomerInfo(dataResponse: CustomerResponse): String {
        spManager.email = dataResponse.email
        spManager.name = dataResponse.name
        spManager.avatarUuid = dataResponse.avatarInfo.uuid
        spManager.avatarUrl = dataResponse.avatarInfo.url
        return dataResponse.avatarInfo.url
    }
}