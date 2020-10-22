package com.pulse.components.pharmacy

import com.pulse.components.cart.CartUseCase
import com.pulse.components.pharmacy.repository.PharmacyRepository

class PharmacyUseCase(private val cartUseCase: CartUseCase, private val repositoryPharmacy: PharmacyRepository) {

    suspend fun pharmacyList(globalProductId: Int) = repositoryPharmacy.pharmacyList(globalProductId)

    suspend fun addProductOrThrow(globalProductId: Int) = cartUseCase.addProductOrThrow(globalProductId)

}