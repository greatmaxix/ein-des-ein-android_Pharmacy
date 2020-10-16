package com.pharmacy.components.pharmacy.repository

import com.pharmacy.data.remote.api.RestApi

class PharmacyRemoteDataSource(private val rm: RestApi) {

    suspend fun globalPharmacyList(globalProductId: Int, page: Int? = null, pageSize: Int? = null) =
        rm.pharmacyList(globalProductId, page, pageSize)
}