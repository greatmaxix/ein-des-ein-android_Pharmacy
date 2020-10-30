package com.pulse.user.repository

import timber.log.Timber

class CustomerRepository(private val lds: CustomerLocalDataSource, private val rds: CustomerRemoteDataSource) {

    @Deprecated("We have to remove this method because it's a part of business logic @Customer != null@, repository provides only data from Data Sources")
    suspend fun isCustomerExist() = lds.isCustomerExist()

    suspend fun getCustomer() = lds.getCustomer()

    suspend fun setCustomerRetrieveAvatarUrl() = try {
        val customer = rds.fetchCustomer().dataOrThrow().item
        lds.save(customer)
        customer.avatarInfo?.url
    } catch (e: Exception) {
        Timber.e("Check customer update")
        null
    }
}