package com.pharmacy.myapp.search.repository

class SearchRepository(private val rds: SearchRemoteDataSource) {

    suspend fun searchPaging(productName: String?, page: Int = 1, pageSize: Int = 10) =
        rds.globalSearch(name = productName, page = page, pageSize = pageSize)
}