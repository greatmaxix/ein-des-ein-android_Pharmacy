package com.pharmacy.myapp.search.repository

import com.pharmacy.myapp.data.remote.rest.RestManager

class SearchRemoteDataSource(private val rm: RestManager) {

    suspend fun globalSearch(page: Int? = null, pageSize: Int? = null, regionId: Int? = null, barCode: Int? = null, name: String? = null) =
        rm.productSearch(page, pageSize, regionId, barCode, name)

}