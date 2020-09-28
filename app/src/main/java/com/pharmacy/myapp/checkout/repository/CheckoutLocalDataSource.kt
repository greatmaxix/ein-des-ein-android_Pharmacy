package com.pharmacy.myapp.checkout.repository

import com.pharmacy.myapp.user.model.customerInfo.CustomerDAO

class CheckoutLocalDataSource(private val dao: CustomerDAO) {

    fun getCustomerInfo() = dao.get()

}