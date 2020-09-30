package com.pharmacy.myapp.user.repository

import com.pharmacy.myapp.user.model.customer.CustomerDAO
import com.pharmacy.myapp.user.model.customer.Customer

class UserLocalDataSource(private val dao: CustomerDAO) {

    suspend fun isCustomerExist() = dao.isCustomerExist()

    suspend fun getCustomer() = dao.getCustomer()

    suspend fun save(customer: Customer) = dao.insert(customer)
}