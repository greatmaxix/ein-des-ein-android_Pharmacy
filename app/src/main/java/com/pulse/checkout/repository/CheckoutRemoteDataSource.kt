package com.pulse.checkout.repository

import com.pulse.data.remote.RestManager
import com.pulse.data.remote.model.order.CreateOrderRequest

class CheckoutRemoteDataSource(private val rm: RestManager) {

    suspend fun sendOrder(body: CreateOrderRequest) = rm.sendOrder(body)

}