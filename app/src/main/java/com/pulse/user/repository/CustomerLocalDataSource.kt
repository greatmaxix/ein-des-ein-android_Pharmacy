package com.pulse.user.repository

import com.pulse.user.model.customer.Customer
import com.pulse.user.model.customer.CustomerDAO

class CustomerLocalDataSource(private val dao: CustomerDAO) {

    suspend fun isCustomerExist() = dao.isCustomerExist()

    suspend fun getCustomer() = dao.getCustomer()

    suspend fun save(customer: Customer) = dao.insert(customer)
}