package com.pharmacy.myapp.splash.repository

import com.pharmacy.myapp.data.local.SPManager
import com.pharmacy.myapp.user.model.customerInfo.Customer
import com.pharmacy.myapp.user.model.customerInfo.CustomerDAO

class SplashLocalDataSource(private val sp: SPManager, private val dao: CustomerDAO) {

    suspend fun saveCustomer(customer: Customer) {
        sp.regionId = customer.region?.regionId
        dao.insert(customer)
    }

    val isTokenExists
        get() = sp.isTokenExists

    val isNeedOnBoarding
        get() = sp.isNeedOnBoarding

}