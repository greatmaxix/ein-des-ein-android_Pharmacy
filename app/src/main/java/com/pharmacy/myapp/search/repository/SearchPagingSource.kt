package com.pharmacy.myapp.search.repository

import androidx.paging.PagingSource
import com.pharmacy.myapp.product.model.ProductLite
import org.koin.core.KoinComponent
import org.koin.core.inject

class SearchPagingSource(private val text: String? = null, private val total: (Int) -> Unit) : PagingSource<Int, ProductLite>(), KoinComponent {

    private val repository: SearchRepository by inject()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, ProductLite> = try {
        repository.searchPaging(text, params.key ?: 1, params.loadSize)
            .run {
                total(data.totalCount)
                LoadResult.Page(data.items, null, data.currentPageNumber + 1)
            }
    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}