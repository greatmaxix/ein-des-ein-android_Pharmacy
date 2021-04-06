package com.pulse.components.user.wishlist.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pulse.components.product.model.ProductLite
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class WishPagingSource : PagingSource<Int, ProductLite>(), KoinComponent {

    private val repository: WishRepository by inject()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductLite> = try {
        val response = repository.getWishList(params.key ?: 1, params.loadSize)
        LoadResult.Page(response.data.items, null, response.data.currentPageNumber + 1)
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Int, ProductLite>) = state.anchorPosition
}