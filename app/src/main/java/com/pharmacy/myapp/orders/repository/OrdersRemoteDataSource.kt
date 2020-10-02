package com.pharmacy.myapp.orders.repository

import com.pharmacy.myapp.data.remote.rest.RestManager

class OrdersRemoteDataSource(private val rm: RestManager) {

    suspend fun fetchOrders(query: String, page: Int? = null, pageSize: Int? = null) = rm.fetchOrders(query, page, pageSize)

    suspend fun getOrderDetail(id: Int) = rm.getOrderDetail(id)
}