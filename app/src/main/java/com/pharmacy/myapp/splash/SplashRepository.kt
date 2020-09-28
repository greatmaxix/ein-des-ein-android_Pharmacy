package com.pharmacy.myapp.splash

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.user.model.customerInfo.CustomerDAO
import com.pharmacy.myapp.user.model.customerInfo.CustomerInfo

class SplashRepository(private val rm: RestManager, private val dao: CustomerDAO, private val sp: SPManager) {

    suspend fun updateCustomerInfo() = safeApiCall(rm.tokenRefreshCall) {
        saveCustomerInfo(rm.fetchCustomerInfo().data.item)
    }

    private suspend fun saveCustomerInfo(customer: CustomerInfo): String? {
        dao.save(customer)
        sp.regionId = customer.region?.regionId
        return customer.avatarInfo?.url
    }
}