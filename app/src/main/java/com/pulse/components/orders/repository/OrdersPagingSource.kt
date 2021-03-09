package com.pulse.components.orders.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pulse.core.network.ResponseWrapper.Error
import com.pulse.core.network.ResponseWrapper.Success
import com.pulse.model.order.Order
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class OrdersPagingSource(val query: String) : PagingSource<Int, Order>(), KoinComponent {

    private val repository: OrdersRepository by inject()

    override suspend fun load(params: LoadParams<Int>) =
        when (val response = repository.fetchOrders(query, params.key ?: 1, params.loadSize)) {
            is Success -> LoadResult.Page(response.value.data.items, null, response.value.data.currentPageNumber + 1)
            is Error -> LoadResult.Error(Exception(response.errorMessage))
        }

    override fun getRefreshKey(state: PagingState<Int, Order>) = state.anchorPosition
}