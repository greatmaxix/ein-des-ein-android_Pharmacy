package com.pharmacy.search.repository

import androidx.paging.PagingSource
import com.pharmacy.core.network.ResponseWrapper.Error
import com.pharmacy.core.network.ResponseWrapper.Success
import com.pharmacy.product.model.ProductLite
import org.koin.core.component.KoinApiExtension
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@KoinApiExtension
class SearchPagingSource(private val text: String? = null, private val total: (Int) -> Unit) : PagingSource<Int, ProductLite>(), KoinComponent {

    private val repository: SearchRepository by inject()

    override suspend fun load(params: LoadParams<Int>) =
        when (val response = repository.searchPaging(text, params.key ?: 1, params.loadSize)) {
            is Success -> {
                total(response.value.data.totalCount)
                LoadResult.Page(response.value.data.items, null, response.value.data.currentPageNumber + 1)
            }
            is Error -> LoadResult.Error(Exception(response.errorMessage))
        }
}