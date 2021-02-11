package com.pulse.components.cart

import com.pulse.components.cart.repository.CartRepository
import com.pulse.components.user.repository.CustomerRepository
import com.pulse.data.GeneralException

class CartUseCase(private val cartRepository: CartRepository, private val repositoryCustomer: CustomerRepository) {

    suspend fun getProducts() =
        repositoryCustomer.getCustomer()?.let { cartRepository.getProducts().items } ?: throw GeneralException.needToLoginCart

    suspend fun removeProduct(productId: Int) =
        cartRepository.removeProduct(productId)

    suspend fun addProductOrThrow(productId: Int) =
        repositoryCustomer.getCustomer()?.let { cartRepository.addProduct(productId) } ?: throw GeneralException.needToLoginAdd
}