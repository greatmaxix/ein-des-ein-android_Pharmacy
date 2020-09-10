package com.pharmacy.myapp.pharmacy.repository

import androidx.paging.PagingSource
import com.pharmacy.myapp.core.network.ResponseWrapper
import com.pharmacy.myapp.pharmacy.model.Pharmacy
import org.koin.core.KoinComponent
import org.koin.core.inject

class PharmacyPagingSource(private val globalProductId: Int) : PagingSource<Int, Pharmacy>(), KoinComponent {

    private val repository: PharmacyRepository by inject()

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Pharmacy> = try {
        when (val response = repository.pharmacyPaging(globalProductId, params.key ?: 1, params.loadSize)) {
            is ResponseWrapper.Success -> LoadResult.Page(response.value.data.items, null, response.value.data.currentPageNumber + 1)
            is ResponseWrapper.Error -> LoadResult.Error(Throwable(response.errorMessage))
        }
    } catch (e: Exception) {
        LoadResult.Error(e)
    }
}