package com.pharmacy.pharmacy.repository

import com.pharmacy.data.remote.RestManager

class PharmacyRemoteDataSource(private val rm: RestManager) {

    suspend fun globalPharmacyList(globalProductId: Int, page: Int? = null, pageSize: Int? = null) =
        rm.getPharmacyList(globalProductId, page, pageSize)
}