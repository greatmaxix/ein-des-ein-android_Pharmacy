package com.pulse.components.orders.details

import com.pulse.data.remote.RestManager


class OrderDetailRemoteDataSource(private val rm: RestManager) {

    suspend fun cancelOrder(id: Int) = rm.cancelOrder(id)

}