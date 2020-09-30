package com.pharmacy.myapp.main

import com.pharmacy.myapp.user.model.customer.CustomerDAO

class MainRepository(private val dao: CustomerDAO) {

    suspend fun isCustomerExist() = dao.isCustomerExist()

    fun customerLiveData() = dao.customerLiveData()
}