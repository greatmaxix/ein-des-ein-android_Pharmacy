package com.pharmacy.myapp.pharmacy.repository

import com.pharmacy.myapp.cart.repository.CartRepository

class PharmacyRepository(private val rds: PharmacyRemoteDataSource, private val cartRepository: CartRepository) {

    suspend fun pharmacyList(globalProductId: Int) = rds.globalPharmacyList(globalProductId)

    suspend fun addToCart(globalProductId: Int) = cartRepository.addProductToCart(globalProductId)
}