package com.pharmacy.myapp.user.repository

import com.pharmacy.myapp.user.model.customerInfo.CustomerDAO

class UserLocalDataSource(private val dao: CustomerDAO) {

    suspend fun isCustomerExist() = dao.isCustomerExist()

}