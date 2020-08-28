package com.pharmacy.myapp.user.repository

class UserRepository(private val lds: UserLocalDataSource) {

    suspend fun isCustomerExist() = lds.isCustomerExist()
}