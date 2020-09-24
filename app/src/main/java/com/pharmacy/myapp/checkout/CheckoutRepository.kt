package com.pharmacy.myapp.checkout

import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.user.model.customerInfo.CustomerDAO

class CheckoutRepository(
    private val rm: RestManager,
    private val dao: CustomerDAO
) {

    fun getCustomerInfo() = dao.get()

    suspend fun sendOrder(mock: String) = rm.sendOrder(mock)
}