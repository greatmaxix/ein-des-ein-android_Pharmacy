package com.pharmacy.myapp.main

import com.pharmacy.myapp.model.customerInfo.CustomerDAO

class MainRepository(private val dao: CustomerDAO) {

    fun getCustomer() = dao.getCustomer()

}