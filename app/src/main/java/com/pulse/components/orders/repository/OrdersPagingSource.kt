package com.pulse.components.orders.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pulse.model.order.Order
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class OrdersPagingSource(val query: String) : PagingSource<Int, Order>(), KoinComponent {

    private val repository: OrdersRepository by inject()

    override suspend fun load(params: LoadParams<Int>) = try {
        val response = repository.fetchOrders(query, params.key ?: 1, params.loadSize)
        LoadResult.Page(response.data.items, null, response.data.currentPageNumber + 1)
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Int, Order>) = state.anchorPosition
}