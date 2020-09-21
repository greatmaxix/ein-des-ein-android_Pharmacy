package com.pharmacy.myapp.cart.repository

class CartRepository(private val rds: CartRemoteDataSource) {

    suspend fun getCartProducts() = rds.getCartProducts()

    suspend fun addProductToCart(globalProductId: Int) = rds.addProductToCart(globalProductId)

    suspend fun removeProductFromCart(globalProductId: Int) = rds.removeProductFromCart(globalProductId)
}