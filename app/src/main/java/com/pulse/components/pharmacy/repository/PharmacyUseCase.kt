package com.pulse.components.pharmacy.repository

import com.pulse.components.cart.CartUseCase

class PharmacyUseCase(private val cartUseCase: CartUseCase, private val repositoryPharmacy: PharmacyRepository) {

    suspend fun pharmacyList(globalProductId: Int) = repositoryPharmacy.pharmacyList(globalProductId)

    suspend fun addProductOrThrow(globalProductId: Int) = cartUseCase.addProductOrThrow(globalProductId)
}