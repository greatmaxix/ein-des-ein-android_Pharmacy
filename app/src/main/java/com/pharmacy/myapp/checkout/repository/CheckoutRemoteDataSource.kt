package com.pharmacy.myapp.checkout.repository

import com.pharmacy.myapp.data.remote.RestManager
import com.pharmacy.myapp.data.remote.model.order.CreateOrderRequest

class CheckoutRemoteDataSource(private val rm: RestManager) {

    suspend fun sendOrder(body: CreateOrderRequest) = rm.sendOrder(body)

}