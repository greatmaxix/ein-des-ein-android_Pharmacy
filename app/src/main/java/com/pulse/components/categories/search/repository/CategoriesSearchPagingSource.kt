package com.pulse.components.categories.search.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pulse.components.product.model.ProductLite
import com.pulse.components.search.repository.SearchRepository
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class CategoriesSearchPagingSource(private val categoryCode: String? = null, private val total: (Int) -> Unit) : PagingSource<Int, ProductLite>(), KoinComponent {

    private val repository: SearchRepository by inject()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductLite> = try {
        val response = repository.searchPaging("", params.key ?: 1, params.loadSize, categoryCode)
        total(response.data.totalCount)
        LoadResult.Page(response.data.items, null, response.data.currentPageNumber + 1)
    } catch (e: Exception) {
        LoadResult.Error(e)
    }

    override fun getRefreshKey(state: PagingState<Int, ProductLite>) = state.anchorPosition
}