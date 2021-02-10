package com.pulse.components.orders.repository

class OrdersRepository(private val srd: OrdersRemoteDataSource) {

    suspend fun fetchOrders(query: String, page: Int, pageSize: Int) = srd.fetchOrders(query, page, pageSize)

    suspend fun getOrderDetail(id: Int) = srd.getOrderDetail(id)
}