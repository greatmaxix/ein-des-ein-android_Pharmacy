package com.pharmacy.checkout.repository

import com.pharmacy.data.remote.RestManager
import com.pharmacy.data.remote.model.order.CreateOrderRequest

class CheckoutRemoteDataSource(private val rm: RestManager) {

    suspend fun sendOrder(body: CreateOrderRequest) = rm.sendOrder(body)

}