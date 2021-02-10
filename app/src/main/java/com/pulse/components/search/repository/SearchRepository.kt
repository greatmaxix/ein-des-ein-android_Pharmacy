package com.pulse.components.search.repository

class SearchRepository(private val rds: SearchRemoteDataSource) {

    suspend fun searchPaging(productName: String?, page: Int = 1, pageSize: Int = 10, categoryCode: String? = null) = rds.globalSearch(productName, page, pageSize, categoryCode)

}