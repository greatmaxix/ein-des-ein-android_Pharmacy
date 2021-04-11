package com.pulse.components.scanner.repository

import com.pulse.data.remote.api.RestApi

class ScannerRemoteDataSource(private val ra: RestApi) {

    suspend fun searchBarcode(barCode: String, regionId: Int?) = ra.productSearch(PAGE, PAGE_SIZE, barCode = barCode, regionId = regionId)

    companion object {
        private const val PAGE = 1
        private const val PAGE_SIZE = 20
    }
}