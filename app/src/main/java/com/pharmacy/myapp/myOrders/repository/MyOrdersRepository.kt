package com.pharmacy.myapp.myOrders.repository

class MyOrdersRepository(private val srd: MyOrdersRemoteDataSource) {

    suspend fun fetchOrders(query: String, page: Int, pageSize: Int) = srd.fetchOrders(query, page, pageSize)

}