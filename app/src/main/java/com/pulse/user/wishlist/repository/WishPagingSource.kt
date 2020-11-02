package com.pulse.user.wishlist.repository

import androidx.paging.PagingSource
import com.pulse.core.network.ResponseWrapper
import com.pulse.product.model.ProductLite
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class WishPagingSource : PagingSource<Int, ProductLite>(), KoinComponent {

    private val repository: WishRepository by inject()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductLite> = try {
        when (val response = repository.getWishList(params.key ?: 1, params.loadSize)) {
            is ResponseWrapper.Success -> LoadResult.Page(response.value.data.items, null, response.value.data.currentPageNumber + 1)
            is ResponseWrapper.Error -> LoadResult.Error(Throwable(response.errorMessage))
        }
    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}