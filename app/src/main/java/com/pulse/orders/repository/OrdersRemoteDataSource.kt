package com.pulse.orders.repository

import com.pulse.data.remote.RestManager


class OrdersRemoteDataSource(private val rm: RestManager) {

    suspend fun fetchOrders(query: String, page: Int? = null, pageSize: Int? = null) = rm.fetchOrders(query, page, pageSize)

    suspend fun getOrderDetail(id: Int) = rm.getOrderDetail(id)
}