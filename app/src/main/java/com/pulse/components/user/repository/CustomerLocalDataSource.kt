package com.pulse.components.user.repository

import com.pulse.components.user.model.customer.Customer
import com.pulse.components.user.model.customer.CustomerDAO

class CustomerLocalDataSource(private val dao: CustomerDAO) {

    @Deprecated("We have to remove this method because it's a part of business logic @Customer != null@, repository provides only data from Data Sources")
    suspend fun isCustomerExist() = dao.isCustomerExist()

    suspend fun getCustomer() = dao.getCustomer()

    suspend fun save(customer: Customer) = dao.insert(customer)
}