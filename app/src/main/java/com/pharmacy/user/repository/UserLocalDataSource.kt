package com.pharmacy.user.repository

import com.pharmacy.user.model.customer.Customer
import com.pharmacy.user.model.customer.CustomerDAO

class UserLocalDataSource(private val dao: CustomerDAO) {

    suspend fun isCustomerExist() = dao.isCustomerExist()

    suspend fun getCustomer() = dao.getCustomer()

    suspend fun save(customer: Customer) = dao.insert(customer)
}