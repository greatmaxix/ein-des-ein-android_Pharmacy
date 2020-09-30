package com.pharmacy.myapp.myOrders.repository

import com.pharmacy.myapp.data.remote.rest.RestManager

class MyOrdersRemoteDataSource(private val rm: RestManager) {

    suspend fun fetchOrders(query: String, page: Int? = null, pageSize: Int? = null) = rm.fetchOrders(query, page, pageSize)

}