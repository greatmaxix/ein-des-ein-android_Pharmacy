package com.pharmacy.cart.repository

import com.pharmacy.user.repository.UserRepository

class CartRepository(private val rds: CartRemoteDataSource, private val repositoryUser: UserRepository) {

    suspend fun getCartProducts() = rds.getCartProducts().dataOrThrow()

    suspend fun addProductToCart(globalProductId: Int) = rds.addProductToCart(globalProductId)

    suspend fun removeProductFromCart(globalProductId: Int) = rds.removeProductFromCart(globalProductId).dataOrThrow()

    suspend fun ddd() {

//        if (isCustomerExist()) getCartProducts().items else throw _errorLiveData.postValue(R.string.forCheckCart)
    }


    // user

    suspend fun isCustomerExist() = repositoryUser.isCustomerExist()
}