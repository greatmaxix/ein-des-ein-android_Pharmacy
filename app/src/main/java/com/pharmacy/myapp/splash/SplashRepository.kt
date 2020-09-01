package com.pharmacy.myapp.splash

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.user.model.customerInfo.CustomerDAO
import com.pharmacy.myapp.user.model.customerInfo.CustomerInfo

class SplashRepository(private val rm: RestManager, private val dao: CustomerDAO) {

    suspend fun updateCustomerInfo() = safeApiCall(rm.tokenRefreshCall) {
        saveCustomerInfo(rm.fetchCustomerInfo().data.item)
    }

    private suspend fun saveCustomerInfo(customer: CustomerInfo): String? {
        dao.save(customer)
        rm.setLocalRegion(customer.region?.regionId)
        return customer.avatarInfo?.url
    }
}