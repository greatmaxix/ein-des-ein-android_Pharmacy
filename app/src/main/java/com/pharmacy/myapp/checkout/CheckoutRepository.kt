package com.pharmacy.myapp.checkout

import com.pharmacy.myapp.checkout.repository.CheckoutLocalDataSource
import com.pharmacy.myapp.checkout.repository.CheckoutRemoteDataSource

class CheckoutRepository(
    private val crds: CheckoutRemoteDataSource,
    private val clds: CheckoutLocalDataSource
) {

    fun getCustomerInfo() = clds.getCustomerInfo()

    suspend fun sendOrder(mock: String) = crds.sendOrder(mock)
}