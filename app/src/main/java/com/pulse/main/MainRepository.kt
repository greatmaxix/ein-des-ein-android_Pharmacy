package com.pulse.main

import com.pulse.user.model.customer.CustomerDAO

class MainRepository(private val dao: CustomerDAO) {

    suspend fun isCustomerExist() = dao.isCustomerExist()

    fun customerLiveData() = dao.customerLiveData()
}