package com.pharmacy.myapp.scanner.repository

import com.pharmacy.myapp.core.network.safeApiCall
import com.pharmacy.myapp.data.remote.rest.RestManager

class ScannerRemoteDataSource(private val rm: RestManager) {

    suspend fun searchBarcode(barCode: String) = safeApiCall(rm.tokenRefreshCall) { rm.productSearch(1, 20, barCode, "") }
}