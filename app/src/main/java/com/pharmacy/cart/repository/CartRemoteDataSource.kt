package com.pharmacy.cart.repository

import com.pharmacy.data.remote.api.RestApi

class CartRemoteDataSource(private val rm: RestApi) {

    suspend fun getCartProducts() = rm.cartProducts()

    suspend fun addProductToCart(globalProductId: Int) = rm.addProductToCart(globalProductId)

    suspend fun removeProductFromCart(globalProductId: Int) = rm.removeProductFromCart(globalProductId)
}