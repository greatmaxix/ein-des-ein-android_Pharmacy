package com.pharmacy.user.repository

class UserRepository(private val lds: UserLocalDataSource) {

    suspend fun isCustomerExist() = lds.isCustomerExist()
}