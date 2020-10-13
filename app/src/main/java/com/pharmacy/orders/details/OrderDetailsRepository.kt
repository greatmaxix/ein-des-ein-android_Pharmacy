package com.pharmacy.orders.details

class OrderDetailsRepository(private val rds: OrderDetailRemoteDataSource) {

    suspend fun cancelOrder(id: Int) = rds.cancelOrder(id)

}