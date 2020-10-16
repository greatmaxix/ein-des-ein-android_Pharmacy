package com.pharmacy.components.pharmacy

import com.pharmacy.components.cart.CartUseCase
import com.pharmacy.components.pharmacy.repository.PharmacyRepository

class PharmacyUseCase(private val cartUseCase: CartUseCase, private val repositoryPharmacy: PharmacyRepository) {

    suspend fun pharmacyList(globalProductId: Int) = repositoryPharmacy.pharmacyList(globalProductId)

    suspend fun addProductOrThrow(globalProductId: Int) = cartUseCase.addProductOrThrow(globalProductId)

}