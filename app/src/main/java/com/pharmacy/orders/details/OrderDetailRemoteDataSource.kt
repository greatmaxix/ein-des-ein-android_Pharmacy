package com.pharmacy.orders.details

import com.pharmacy.data.remote.RestManager


class OrderDetailRemoteDataSource(private val rm: RestManager) {

    suspend fun cancelOrder(id: Int) = rm.cancelOrder(id)

}