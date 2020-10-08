package com.pharmacy.myapp.myOrders.repository

import androidx.paging.PagingSource
import com.pharmacy.myapp.core.network.ResponseWrapper.Error
import com.pharmacy.myapp.core.network.ResponseWrapper.Success
import com.pharmacy.myapp.model.order.Order
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class MyOrdersPagingSource(val query: String) : PagingSource<Int, Order>(), KoinComponent {

    private val repository: MyOrdersRepository by inject()

    override suspend fun load(params: LoadParams<Int>) =
        when (val response = repository.fetchOrders(query, params.key ?: 1, params.loadSize)) {
            is Success -> LoadResult.Page(response.value.data.items, null, response.value.data.currentPageNumber + 1)
            is Error -> LoadResult.Error(Exception(response.errorMessage))
        }
}