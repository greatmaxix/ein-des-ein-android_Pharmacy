package com.pulse.components.categories.search.repository

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.pulse.components.product.model.ProductLite
import com.pulse.components.search.repository.SearchRepository
import com.pulse.core.network.ResponseWrapper.Error
import com.pulse.core.network.ResponseWrapper.Success
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class CategoriesSearchPagingSource(private val categoryCode: String? = null, private val total: (Int) -> Unit) : PagingSource<Int, ProductLite>(), KoinComponent {

    private val repository: SearchRepository by inject()

    override suspend fun load(params: LoadParams<Int>) =
        when (val response = repository.searchPaging("", params.key ?: 1, params.loadSize, categoryCode)) {
            is Success -> {
                total(response.value.data.totalCount)
                LoadResult.Page(response.value.data.items, null, response.value.data.currentPageNumber + 1)
            }
            is Error -> LoadResult.Error(Exception(response.errorMessage))
        }

    override fun getRefreshKey(state: PagingState<Int, ProductLite>) = state.anchorPosition
}