package com.pharmacy.myapp.scanner.repository

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.remote.rest.RestManager

class ScannerRemoteDataSource(private val rm: RestManager) {

    suspend fun searchBarcode(barCode: String) = rm.productSearch(PAGE, PAGE_SIZE, barCode)

    companion object {
        private const val PAGE = 1
        private const val PAGE_SIZE = 20
    }
}