package com.pharmacy.myapp.orders.repository

import androidx.paging.PagingSource
import com.pharmacy.myapp.core.network.ResponseWrapper.Error
import com.pharmacy.myapp.core.network.ResponseWrapper.Success
import com.pharmacy.myapp.model.order.Order
import org.koin.core.KoinComponent
import org.koin.core.inject

class OrdersPagingSource(val query: String) : PagingSource<Int, Order>(), KoinComponent {

    private val repository: OrdersRepository by inject()

    override suspend fun load(params: LoadParams<Int>) =
        when (val response = repository.fetchOrders(query, params.key ?: 1, params.loadSize)) {
            is Success -> LoadResult.Page(response.value.data.items, null, response.value.data.currentPageNumber + 1)
            is Error -> LoadResult.Error(Exception(response.errorMessage))
        }
}