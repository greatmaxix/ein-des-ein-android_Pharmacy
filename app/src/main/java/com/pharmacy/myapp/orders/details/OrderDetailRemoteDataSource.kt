package com.pharmacy.myapp.orders.details

import com.pharmacy.myapp.data.remote.RestManager


class OrderDetailRemoteDataSource(private val rm: RestManager) {

    suspend fun cancelOrder(id: Int) = rm.cancelOrder(id)

}