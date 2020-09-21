package com.pharmacy.myapp.cart.repository

import com.pharmacy.myapp.data.remote.rest.RestManager

class CartRemoteDataSource(private val rm: RestManager) {
    suspend fun getCartProducts() = rm.getCartProducts()

    suspend fun addProductToCart(globalProductId: Int) = rm.addProductToCart(globalProductId)

    suspend fun removeProductFromCart(globalProductId: Int) = rm.removeProductFromCart(globalProductId)
}