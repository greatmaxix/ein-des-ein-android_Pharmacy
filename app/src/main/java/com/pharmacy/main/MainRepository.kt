package com.pharmacy.main

import com.pharmacy.user.model.customer.CustomerDAO

class MainRepository(private val dao: CustomerDAO) {

    suspend fun isCustomerExist() = dao.isCustomerExist()

    fun customerLiveData() = dao.customerLiveData()
}