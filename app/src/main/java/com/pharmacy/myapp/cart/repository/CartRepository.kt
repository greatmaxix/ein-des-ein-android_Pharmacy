package com.pharmacy.myapp.cart.repository

import com.pharmacy.myapp.user.repository.UserRepository

class CartRepository(private val rds: CartRemoteDataSource, private val repositoryUser: UserRepository) {

    suspend fun getCartProducts() = rds.getCartProducts()

    suspend fun addProductToCart(globalProductId: Int) = rds.addProductToCart(globalProductId)

    suspend fun removeProductFromCart(globalProductId: Int) = rds.removeProductFromCart(globalProductId)

    // user

    suspend fun isCustomerExist() = repositoryUser.isCustomerExist()
}