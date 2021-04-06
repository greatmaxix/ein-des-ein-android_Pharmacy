package com.pulse.components.checkout.repository

import com.pulse.data.remote.api.RestApi
import com.pulse.data.remote.model.order.CreateOrderRequest

class CheckoutRemoteDataSource(private val ra: RestApi) {

    suspend fun sendOrder(body: CreateOrderRequest) = ra.sendOrder(body)
}