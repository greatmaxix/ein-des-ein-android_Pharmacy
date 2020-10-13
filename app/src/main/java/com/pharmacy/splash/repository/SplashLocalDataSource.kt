package com.pharmacy.splash.repository

import com.pharmacy.data.local.SPManager
import com.pharmacy.user.model.customer.Customer
import com.pharmacy.user.model.customer.CustomerDAO

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