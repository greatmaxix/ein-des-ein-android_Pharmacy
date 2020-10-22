package com.pulse.components.cart.repository

class CartRepository(private val rds: CartRemoteDataSource) {

    suspend fun getProducts() = rds.getCartProducts().dataOrThrow()

    suspend fun addProduct(globalProductId: Int) = rds.addProductToCart(globalProductId).dataOrThrow()

    suspend fun removeProduct(globalProductId: Int) = rds.removeProductFromCart(globalProductId).dataOrThrow()

}