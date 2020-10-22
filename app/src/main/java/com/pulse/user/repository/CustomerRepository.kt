package com.pulse.user.repository

class CustomerRepository(private val lds: CustomerLocalDataSource) {

    @Deprecated("We have to remove this method because it's a part of business logic @Customer != null@, repository provides only data from Data Sources")
    suspend fun isCustomerExist() = lds.isCustomerExist()

    suspend fun getCustomer() = lds.getCustomer()
}