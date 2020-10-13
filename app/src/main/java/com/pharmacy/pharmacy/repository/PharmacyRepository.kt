package com.pharmacy.pharmacy.repository

import com.pharmacy.cart.repository.CartRepository
import com.pharmacy.user.repository.UserRepository

class PharmacyRepository(private val rds: PharmacyRemoteDataSource, private val cartRepository: CartRepository, private val repositoryUser: UserRepository) {

    suspend fun pharmacyList(globalProductId: Int) = rds.globalPharmacyList(globalProductId)

    suspend fun addToCart(globalProductId: Int) = cartRepository.addProductToCart(globalProductId)

    // user

    suspend fun isCustomerExist() = repositoryUser.isCustomerExist()
}