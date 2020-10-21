package com.pulse.user.repository

class CustomerRepository(private val lds: CustomerLocalDataSource) {

    @Deprecated("")
    suspend fun isCustomerExist() = lds.isCustomerExist()

    suspend fun getCustomer() = lds.getCustomer()
}