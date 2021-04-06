package com.pulse.components.orders.repository

import com.pulse.data.remote.api.RestApi


class OrdersRemoteDataSource(private val ra: RestApi) {

    suspend fun fetchOrders(query: String, page: Int? = null, pageSize: Int? = null) = ra.fetchOrders(query, page, pageSize)

    suspend fun getOrderDetail(id: Int) = ra.getOrderDetail(id)
}