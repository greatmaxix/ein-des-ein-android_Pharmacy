package com.pharmacy.splash.repository

import com.pharmacy.core.network.ResponseWrapper
import com.pharmacy.user.model.customer.Customer

class SplashRepository(private val rds: SplashRemoteDataSource, private val lds: SplashLocalDataSource) {

    suspend fun setCustomerRetrieveAvatarUrl() =
        if (lds.isTokenExists) {
            when (val response = rds.fetchCustomer()) {
                is ResponseWrapper.Success -> saveCustomer(response.value.data.item)
                is ResponseWrapper.Error -> throw Exception("Check customer update")
            }
        } else null

    private suspend fun saveCustomer(customer: Customer): String? {
        lds.saveCustomer(customer)
        return customer.avatarInfo?.url
    }

    val isNeedOnBoarding
        get() = lds.isNeedOnBoarding
}
