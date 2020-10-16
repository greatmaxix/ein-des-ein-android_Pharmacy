package com.pharmacy.components.cart

import com.pharmacy.components.cart.repository.CartRepository
import com.pharmacy.data.GeneralException
import com.pharmacy.user.repository.CustomerRepository

class CartUseCase(private val cartRepository: CartRepository, private val repositoryCustomer: CustomerRepository) {

    suspend fun getProducts() =
        repositoryCustomer.getCustomer()?.let { cartRepository.getProducts().items } ?: throw GeneralException.needToLoginCart

    suspend fun removeProduct(productId: Int) =
        cartRepository.removeProduct(productId)

    suspend fun addProductOrThrow(productId: Int) =
        repositoryCustomer.getCustomer()?.let { cartRepository.addProduct(productId) } ?: throw GeneralException.needToLoginAdd
}