package com.pharmacy.myapp.checkout

import com.pharmacy.myapp.checkout.repository.CheckoutLocalDataSource
import com.pharmacy.myapp.checkout.repository.CheckoutRemoteDataSource
import com.pharmacy.myapp.data.remote.rest.request.order.CreateOrderRequest

class CheckoutRepository(
    private val crds: CheckoutRemoteDataSource,
    private val clds: CheckoutLocalDataSource
) {

    fun getCustomerInfo() = clds.getCustomerInfo()

    suspend fun sendOrder(body: CreateOrderRequest) = crds.sendOrder(body)
}