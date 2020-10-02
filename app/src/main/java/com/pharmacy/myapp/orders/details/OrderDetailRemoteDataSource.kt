package com.pharmacy.myapp.orders.details

import com.pharmacy.myapp.data.remote.rest.RestManager

class OrderDetailRemoteDataSource(private val rm: RestManager) {

    suspend fun cancelOrder(id: Int) = rm.cancelOrder(id)

}