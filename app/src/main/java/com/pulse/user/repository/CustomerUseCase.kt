package com.pulse.user.repository

import com.pulse.data.local.SPManager

class CustomerUseCase(private val repositoryCustomer: CustomerRepository, private val sp: SPManager) {

    suspend fun setCustomerRetrieveAvatarUrl() = if (sp.isTokenExists) repositoryCustomer.setCustomerRetrieveAvatarUrl() else null
}