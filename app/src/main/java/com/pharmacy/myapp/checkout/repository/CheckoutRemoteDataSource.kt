package com.pharmacy.myapp.checkout.repository

import com.pharmacy.myapp.data.remote.rest.RestManager
import com.pharmacy.myapp.orders.model.CreateOrderRequest

class CheckoutRemoteDataSource(private val rm: RestManager) {

    suspend fun sendOrder(body: CreateOrderRequest) = rm.sendOrder(body)

}