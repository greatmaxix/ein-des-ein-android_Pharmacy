package com.pharmacy.components.pharmacy.repository

class PharmacyRepository(private val rds: PharmacyRemoteDataSource) {

    suspend fun pharmacyList(globalProductId: Int) = rds.globalPharmacyList(globalProductId).dataOrThrow()

}