package com.pharmacy.search.repository

import com.pharmacy.data.remote.RestManager

class SearchRemoteDataSource(private val rm: RestManager) {

    suspend fun globalSearch(name: String? = null, page: Int? = null, pageSize: Int? = null, categoryCode: String? = null) =
        rm.productSearch(page, pageSize, name = name, categoryCode = categoryCode)

}