package com.pharmacy.myapp.main

import com.pharmacy.myapp.user.model.customerInfo.CustomerDAO

class MainRepository(private val dao: CustomerDAO) {

    suspend fun isCustomerExist() = dao.isCustomerExist()

    fun getCustomerInfo() = dao.get()
}