package com.pharmacy.myapp.pharmacy.repository

class PharmacyRepository(private val rds: PharmacyRemoteDataSource) {

    suspend fun pharmacyPaging(globalProductId: Int, page: Int = 1, pageSize: Int = 10) = rds.globalPharmacyList(globalProductId, page, pageSize)

}