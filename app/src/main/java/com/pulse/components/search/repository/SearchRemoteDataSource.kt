package com.pulse.components.search.repository

import com.pulse.data.remote.api.RestApi

class SearchRemoteDataSource(private val ra: RestApi) {

    suspend fun globalSearch(name: String? = null, page: Int? = null, pageSize: Int? = null, categoryCode: String? = null, regionId: Int?) =
        ra.productSearch(page, pageSize, name = name, categoryCode = categoryCode, regionId = regionId)
}